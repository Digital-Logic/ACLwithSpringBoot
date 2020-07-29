package net.digitallogic.AclRestUser.eventListeners;

import lombok.extern.slf4j.Slf4j;
import net.digitallogic.AclRestUser.persistence.entity.AuthorityEntity;
import net.digitallogic.AclRestUser.persistence.entity.RoleEntity;
import net.digitallogic.AclRestUser.persistence.repository.AuthorityRepository;
import net.digitallogic.AclRestUser.persistence.repository.RoleRepository;
import net.digitallogic.AclRestUser.security.Authority;
import net.digitallogic.AclRestUser.security.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Component
public class InitialAppSetup {

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    @Autowired
    public InitialAppSetup(RoleRepository roleRepository, AuthorityRepository authorityRepository) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
    }

    @EventListener
    public void onApplicationReadyEvent(ApplicationReadyEvent event) {
        // validate roles and authorities
        Map<String, RoleEntity> roles = StreamSupport.stream(
                roleRepository.findAll().spliterator(),false)
                .collect(Collectors.toMap(RoleEntity::getName, Function.identity()));

        List<String> missingRoles =  Arrays.stream(Roles.values())
                .map(Roles::name)
                .filter(Predicate.not(roles::containsKey))
                .collect(Collectors.toList());

        if (!missingRoles.isEmpty()) {
            throw new RuntimeException("Role database is not constant with Roles: " + missingRoles);
        }

        Map<String, AuthorityEntity> authorities = StreamSupport.stream(
                authorityRepository.findAll().spliterator(), false
        ).collect(Collectors.toMap(AuthorityEntity::getName, Function.identity()));


        List<String> missingAuthorities = Arrays.stream(Authority.values())
                .map(Authority::name)
                .filter(Predicate.not(authorities::containsKey))
                .collect(Collectors.toList());

        if (!missingAuthorities.isEmpty()) {
            throw new RuntimeException("Authorities entities in database is not correct.");
        }
    }
}
