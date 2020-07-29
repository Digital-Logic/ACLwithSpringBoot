package net.digitallogic.AclRestUser.fixtures;

import net.digitallogic.AclRestUser.persistence.entity.AuthorityEntity;
import net.digitallogic.AclRestUser.security.Authority;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AuthorityFixtures {

    private static int index=1;

    private static Map<String, AuthorityEntity> authorities =
            Arrays.stream(Authority.values())
                .map(auth -> AuthorityEntity.builder()
                        .id(index++)
                        .name(auth.name)
                        .build()
                )
            .collect(Collectors.toMap(AuthorityEntity::getName, Function.identity()));

    private static AuthorityEntity duplicate(AuthorityEntity auth) {
        return auth.toBuilder().build();
    }

    public static List<AuthorityEntity> getAuthorities () {
        return authorities.values().stream()
                .map(AuthorityFixtures::duplicate)
                .collect(Collectors.toList());
    }

    public static AuthorityEntity getAuthority(String name) {
        return duplicate(authorities.get(name));
    }
}
