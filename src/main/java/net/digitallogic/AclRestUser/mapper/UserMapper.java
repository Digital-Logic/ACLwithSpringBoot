package net.digitallogic.AclRestUser.mapper;


import net.digitallogic.AclRestUser.persistence.entity.UserEntity;
import net.digitallogic.AclRestUser.web.DTO.UserDto;
import net.digitallogic.AclRestUser.web.request.CreateUser;
import net.digitallogic.AclRestUser.web.response.UserResponse;

import java.util.List;

//@Mapper(config = MapperConfig.class,
//    uses = RoleMapper.class
//)
public interface UserMapper {

    UserDto toDto(CreateUser createUser);
 //   @Mapping(target = "password", ignore = true)
    UserDto toDto(UserEntity userEntity);
    UserEntity toEntity(UserDto user);
    UserResponse toResponse(UserDto userDto);

    List<UserDto> toDto(Iterable<UserEntity> userEntities);
    List<UserResponse> toResponse(Iterable<UserDto> userDtos);
}
