package net.digitallogic.AclRestUser.fixtures;

import net.digitallogic.AclRestUser.persistence.entity.RoleEntity;
import net.digitallogic.AclRestUser.security.Authority;
import net.digitallogic.AclRestUser.security.Roles;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RoleFixtures {

    private static Map<String, RoleEntity> roles = Stream.of(
        RoleEntity.builder().id(1).name(Roles.ADMIN.name)
                .authorities(Set.of(AuthorityFixtures.getAuthority(Authority.ADMIN_USER.name)))
                .build(),

        RoleEntity.builder().id(2).name(Roles.USER.name).build()
    ).collect(Collectors.toMap(RoleEntity::getName, Function.identity()));

    private static RoleEntity duplicate(RoleEntity roleEntity) {
        return roleEntity.toBuilder().build();
    }
    public static RoleEntity getRole(String role) {
        return duplicate(
            roles.get(role)
        );
    }
}
