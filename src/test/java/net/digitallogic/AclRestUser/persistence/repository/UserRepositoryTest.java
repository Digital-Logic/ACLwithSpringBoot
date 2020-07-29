package net.digitallogic.AclRestUser.persistence.repository;


import net.digitallogic.AclRestUser.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(UserRepositoryTest.UserRepositoryTestConfig.class)
@Sql(value = "classpath:repository/insertUserJoeDirt.sql")
public class UserRepositoryTest {

    @TestConfiguration
    public static class UserRepositoryTestConfig {

        private final String encoderId;
        private final int bCryptRounds;

        @Autowired
        public UserRepositoryTestConfig(
                @Value("${password.encoder}") String encoderId,
                @Value("${bCryptRounds}") int bCryptRounds) {

            this.encoderId = encoderId;
            this.bCryptRounds = bCryptRounds;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            Map<String, PasswordEncoder> encoders = Map.of(
                    "bcrypt", new BCryptPasswordEncoder(12)
            );

            return new DelegatingPasswordEncoder("bcrypt", encoders);
        }
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    public void testDefaultAdminUser() {
        Optional<UserEntity> userOptional = userRepository.findByEmailIgnoreCase("admin@localhost");

        assertThat(userOptional).isNotEmpty();

        // valid default password
        assertThat(passwordEncoder.matches(
                "password",
                userOptional.get().getEncodedPassword())
        ).isTrue();
    }

    @Test
    public void findByEmailIgnoreCaseTest() {
        Optional<UserEntity> userOptional = userRepository
                .findByEmailIgnoreCase("ADMIN@localHost");

        assertThat(userOptional).isNotEmpty();
    }


    @Test
    public void findByEmailIgnoreCaseTestUser() {
        Optional<UserEntity> userEntityOptional = userRepository
                .findByEmailIgnoreCase("joe@dirt.com");

        assertThat(userEntityOptional).isNotEmpty();

        PersistenceUtil pu = Persistence.getPersistenceUtil();

        // Roles should not be loaded
        assertThat(pu.isLoaded(userEntityOptional.get().getRoles()))
                .isFalse();
    }

    @Test
    public void findByIdWithRolesTest() {
        Optional<UserEntity> temp = userRepository.findByEmailIgnoreCase("joe@dirt.com");
        Optional<UserEntity> userEntityOptional = userRepository.findByIdWithRoles(temp.get().getId());

        assertThat(userEntityOptional).isNotEmpty();

        PersistenceUtil pu = Persistence.getPersistenceUtil();

        assertThat(pu.isLoaded(userEntityOptional.get().getRoles())).isTrue();
        assertThat(userEntityOptional.get().getRoles()).isNotEmpty();

        userEntityOptional.get().getRoles().stream()
                .forEach(role -> {
                    assertThat(pu.isLoaded(role.getAuthorities())).isFalse();
                });
    }

    public void getUserWithAuthoritiesTest() {
        Optional<UserEntity> temp = userRepository.findByEmailIgnoreCase("admin@localhost");
        Optional<UserEntity> userEntityOptional = userRepository.findByIdWithRolesAndAuthorities(temp.get().getId());

        PersistenceUtil pu = Persistence.getPersistenceUtil();

        assertThat(userEntityOptional).isNotEmpty();
        userEntityOptional.get().getRoles().forEach(role -> {
            assertThat(pu.isLoaded(role.getAuthorities())).isTrue();
        });
    }

}
