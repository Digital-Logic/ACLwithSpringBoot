package net.digitallogic.AclRestUser.persistence.repository;

import net.digitallogic.AclRestUser.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);

    @Query("SELECT user FROM UserEntity user " +
            "LEFT JOIN FETCH user.roles roles " +
            "LEFT JOIN FETCH roles.authorities authorities " +
            "WHERE user.email = :email")
    Optional<UserEntity> findByEmailIgnoreCaseWithAuthorities(@Param("email") String email);

    @Query("SELECT user FROM UserEntity user " +
            "LEFT JOIN FETCH user.roles roles " +
            "WHERE user.id = :id")
    Optional<UserEntity> findByIdWithRoles(@Param("id") long id);


    @Query("SELECT user FROM UserEntity user " +
            "LEFT JOIN FETCH user.roles roles " +
            "LEFT JOIN FETCH roles.authorities authorities " +
            "WHERE user.id = :id")
    Optional<UserEntity> findByIdWithRolesAndAuthorities(@Param("id") long id);
}
