package net.digitallogic.AclRestUser.services;

import lombok.extern.slf4j.Slf4j;
import net.digitallogic.AclRestUser.mapper.UserMapper;
import net.digitallogic.AclRestUser.persistence.entity.RoleEntity;
import net.digitallogic.AclRestUser.persistence.entity.UserEntity;
import net.digitallogic.AclRestUser.persistence.repository.RoleRepository;
import net.digitallogic.AclRestUser.persistence.repository.UserRepository;
import net.digitallogic.AclRestUser.security.Roles;
import net.digitallogic.AclRestUser.web.DTO.UserDto;
import net.digitallogic.AclRestUser.web.exceptions.BadRequest;
import net.digitallogic.AclRestUser.web.exceptions.ServerError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Stream;

@RestController
@Slf4j
@Primary
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;
    private final RoleRepository roleRepository;
    private final MutableAclService aclService;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper,
                       PasswordEncoder passwordEncoder, MessageSource messageSource, RoleRepository roleRepository, MutableAclService aclService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.messageSource = messageSource;
        this.roleRepository = roleRepository;
        this.aclService = aclService;
    }

    @Transactional
    public UserDto createUser(UserDto userData) {

        if (userRepository.existsByEmailIgnoreCase(userData.getEmail()))
            throw new BadRequest(getMessage("exception.duplicateUserAccount", userData.getEmail()));

        UserEntity user = userMapper.toEntity(userData);

        user.setEncodedPassword(passwordEncoder.encode(userData.getPassword()));
        user.setAccountEnabled(true); // Enable account by default for now....

        RoleEntity userRole = roleRepository.findByName(Roles.USER.name)
            .orElseThrow(() -> {
                log.error("Error creating user account, unable to assign the USER role to new account.");
                return new ServerError();
            });

        user.addRole(userRole);

        UserEntity newUser = userRepository.save(user);

        /*
         * Setup Acl entries
         */

        ObjectIdentity oi = new ObjectIdentityImpl(UserEntity.class, newUser.getId());
        Sid userSid = new PrincipalSid(newUser.getEmail());

        MutableAcl acl = aclService.createAcl(oi);

        Stream.of(BasePermission.READ, BasePermission.DELETE, BasePermission.WRITE)
                .forEach(permission ->
                    acl.insertAce(acl.getEntries().size(), permission, userSid, true)
                );
        aclService.updateAcl(acl);

        /*
         * Return new User
         */
        return userMapper.toDto(newUser);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public UserDto getUser(long id) {
        return userMapper.toDto(
            userRepository.findById(id)
                .orElseThrow(() ->
                        new BadRequest(getMessage("exception.entityDoesNotExist", "User", id))
                )
        );
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<UserDto> getAllUsers() {
        return userMapper.toDto(
            userRepository.findAll()
        );
    }


    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findByEmailIgnoreCaseWithAuthorities(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));
    }

    private String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
