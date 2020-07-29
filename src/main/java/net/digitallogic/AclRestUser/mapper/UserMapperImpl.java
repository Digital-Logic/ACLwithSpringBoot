package net.digitallogic.AclRestUser.mapper;

import net.digitallogic.AclRestUser.persistence.entity.RoleEntity;
import net.digitallogic.AclRestUser.persistence.entity.UserEntity;
import net.digitallogic.AclRestUser.web.DTO.RoleDto;
import net.digitallogic.AclRestUser.web.DTO.UserDto;
import net.digitallogic.AclRestUser.web.request.CreateUser;
import net.digitallogic.AclRestUser.web.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class UserMapperImpl implements UserMapper {

    private final RoleMapper roleMapper;

    @Autowired
    public UserMapperImpl(RoleMapper roleMapper) {

        this.roleMapper = roleMapper;
    }

    @Override
    public UserDto toDto(CreateUser createUser) {
        if ( createUser == null ) {
            return null;
        }

        UserDto.UserDtoBuilder userDto = UserDto.builder();

        userDto.email( createUser.getEmail() );
        userDto.password( createUser.getPassword() );
        userDto.accountExpired( createUser.isAccountExpired() );
        userDto.accountDisabled( createUser.isAccountDisabled() );
        userDto.credentialsExpired( createUser.isCredentialsExpired() );
        userDto.accountEnabled( createUser.isAccountEnabled() );

        return userDto.build();
    }

    @Override
    public UserDto toDto(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        UserDto.UserDtoBuilder userDto = UserDto.builder();

        userDto.id( userEntity.getId() );
        userDto.email( userEntity.getEmail() );
        userDto.accountExpired( userEntity.isAccountExpired() );
        userDto.credentialsExpired( userEntity.isCredentialsExpired() );
        userDto.accountEnabled( userEntity.isAccountEnabled() );

        PersistenceUtil pu = Persistence.getPersistenceUtil();

        if (pu.isLoaded(userEntity.getRoles())) {
            userDto.roles(roleMapper.toDto(userEntity.getRoles()));
        }

        return userDto.build();
    }

    @Override
    public UserEntity toEntity(UserDto user) {
        if ( user == null ) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        userEntity.id( user.getId() );
        userEntity.email( user.getEmail() );
        userEntity.accountExpired( user.isAccountExpired() );
        userEntity.credentialsExpired( user.isCredentialsExpired() );
        userEntity.accountEnabled( user.isAccountEnabled() );
        userEntity.roles( roleDtoListToRoleEntitySet( user.getRoles() ) );

        return userEntity.build();
    }

    @Override
    public UserResponse toResponse(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.id( userDto.getId() );
        userResponse.email( userDto.getEmail() );
        userResponse.accountExpired( userDto.isAccountExpired() );
        userResponse.accountDisabled( userDto.isAccountDisabled() );
        userResponse.credentialsExpired( userDto.isCredentialsExpired() );
        userResponse.accountEnabled( userDto.isAccountEnabled() );
        userResponse.roles( roleMapper.toString( userDto.getRoles() ) );

        return userResponse.build();
    }

    @Override
    public List<UserDto> toDto(Iterable<UserEntity> userEntities) {
        if ( userEntities == null ) {
            return null;
        }

        List<UserDto> list = new ArrayList<UserDto>();
        for ( UserEntity userEntity : userEntities ) {
            list.add( toDto( userEntity ) );
        }

        return list;
    }

    @Override
    public List<UserResponse> toResponse(Iterable<UserDto> userDtos) {
        if ( userDtos == null ) {
            return null;
        }

        List<UserResponse> list = new ArrayList<UserResponse>();
        for ( UserDto userDto : userDtos ) {
            list.add( toResponse( userDto ) );
        }

        return list;
    }

    protected Set<RoleEntity> roleDtoListToRoleEntitySet(List<RoleDto> list) {
        if ( list == null ) {
            return null;
        }

        Set<RoleEntity> set = new HashSet<RoleEntity>( Math.max( (int) ( list.size() / .75f ) + 1, 16 ) );
        for ( RoleDto roleDto : list ) {
            set.add( roleMapper.toEntity( roleDto ) );
        }

        return set;
    }
}
