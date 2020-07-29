package net.digitallogic.AclRestUser.mapper;

import net.digitallogic.AclRestUser.persistence.entity.RoleEntity;
import net.digitallogic.AclRestUser.web.DTO.RoleDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapperConfig.class,
    uses = {AuthorityMapper.class}
)
public interface RoleMapper {

    RoleDto toDto(RoleEntity roleEntity);
    RoleEntity toEntity(RoleDto roleDto);

    default String toString(RoleDto roleDto) {
        return roleDto.getName();
    }

    List<RoleDto> toDto(Iterable<RoleEntity> roles);
    List<String> toString(Iterable<RoleDto> roles);
}
