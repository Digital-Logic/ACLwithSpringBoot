package net.digitallogic.AclRestUser.persistence.repository;

import net.digitallogic.AclRestUser.persistence.entity.AuthorityEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthorityRepository extends CrudRepository<AuthorityEntity, Long> {

    Optional<AuthorityEntity> findByName(String name);
}
