package net.digitallogic.AclRestUser.mapper;

import net.digitallogic.AclRestUser.persistence.entity.AuthorityEntity;
import net.digitallogic.AclRestUser.web.DTO.AuthorityDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface AuthorityMapper {

    AuthorityDto toDto(AuthorityEntity authorityEntity);
    AuthorityEntity toEntity(AuthorityDto authorityDto);

    List<AuthorityDto> toDto(Iterable<AuthorityEntity> authorityEntities);
}
