package net.digitallogic.AclRestUser.persistence.entity;


import net.digitallogic.AclRestUser.fixtures.UserFixtures;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserEntityTest {

    @Test
    public void equalsAndHash() {
        UserEntity user = UserFixtures.getUserEntity();
        UserEntity copy = UserEntity.builder().id(user.getId()).build();

        assertThat(user).hasSameHashCodeAs(copy);
        assertThat(user).isEqualTo(copy);
    }

    @Test
    public void builderTest() {
        UserEntity.UserEntityBuilder builder = UserFixtures.getUserEntityBuilder();
        UserEntity user = builder.build();
        assertThat(user.getId()).isNotNull();
    }
}
