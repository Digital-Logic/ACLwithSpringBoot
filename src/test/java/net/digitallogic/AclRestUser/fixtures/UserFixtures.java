package net.digitallogic.AclRestUser.fixtures;

import net.digitallogic.AclRestUser.persistence.entity.UserEntity;
import net.digitallogic.AclRestUser.web.request.CreateUser;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class UserFixtures {
    private static Random random = new Random();

    private static List<UserEntity> users = List.of(
            UserEntity.builder().id(1).email("Joe@exotic.com").encodedPassword("jlsadjf").build(),
            UserEntity.builder().id(2).email("JohnWick@BadAss.com").encodedPassword("hello").build(),
            UserEntity.builder().id(3).email("sarah@conner.com").encodedPassword("password").build(),
            UserEntity.builder().id(4).email("bob@barker.com").encodedPassword("password").build(),
            UserEntity.builder().id(5).email("howard@theDuck.com").encodedPassword("duck").build()
    );

    public static List<UserEntity.UserEntityBuilder> getUserBuilders() {
        return getUserBuilders(users.size());
    }

    public static List<UserEntity.UserEntityBuilder> getUserBuilders(int size) {
        return users.stream().limit(size)
                .map(UserEntity::toBuilder)
                .collect(Collectors.toList());
    }

    public static List<UserEntity> getUsers() {
        return getUsers(users.size());
    }
    public static List<UserEntity> getUsers(int size) {
        return users.stream().limit(size)
                .map(UserEntity::toBuilder)
                .map(UserEntity.UserEntityBuilder::build)
                .collect(Collectors.toList());
    }

    public static UserEntity.UserEntityBuilder getUserEntityBuilder() {
        return users.get(random.nextInt(users.size()))
                .toBuilder();
    }

    public static UserEntity getUserEntity() {
        return getUserEntityBuilder().build();
    }

    public static CreateUser getCreateUser() {
        UserEntity user = users.get(random.nextInt(users.size()));

        return CreateUser.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
