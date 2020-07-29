package net.digitallogic.AclRestUser.web.controller;

import net.digitallogic.AclRestUser.fixtures.UserFixtures;
import net.digitallogic.AclRestUser.persistence.entity.UserEntity;
import net.digitallogic.AclRestUser.persistence.repository.UserRepository;
import net.digitallogic.AclRestUser.web.request.CreateUser;
import net.digitallogic.AclRestUser.web.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@Sql(value = "classpath:repository/insertUserJoeDirt.sql")
@ActiveProfiles({"H2", "TEST"})
public class UserControllerSecurityTest {

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithAnonymousUser
    public void createUserTest() {
        CreateUser newUser = UserFixtures.getCreateUser();
        UserResponse response = userController.createUser(newUser);
        assertThat(response).isNotNull();
    }

    @Test
    @WithAnonymousUser
    public void getAllUsersWithAnonymousUserTest() {
        assertThatThrownBy(() -> {
            List<UserResponse> users = userController.getUsers();
        }).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithMockUser(value = "adminUser", authorities = {"ADMIN_USER"})
    public void getAllUsersWithAuthorizedUserTest() {
        List<UserResponse> users = userController.getUsers();
        assertThat(users).isNotEmpty();
    }

    @Test
    @WithMockUser(value = "RandomUser")
    public void getAllUsersTest() {
        assertThatThrownBy(() -> {
            List<UserResponse> users = userController.getUsers();
        }).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithUserDetails(value = "joe@dirt.com",
            userDetailsServiceBeanName = "userServiceImpl")
    public void getUserTest() {
        Optional<UserEntity> userOptional = userRepository.findByEmailIgnoreCase("joe@dirt.com");
        assertThat(userOptional).isNotEmpty();
        UserEntity user = userOptional.get();

        UserResponse userResponse = userController.getUser(user.getId());

        assertThat(userResponse).isNotNull();
        assertThat(userResponse).isEqualToComparingOnlyGivenFields(user,
                "id", "email");
    }

    @Test
    @WithUserDetails(value = "steve@yahoo.com")
    public void getAnotherUserTest() {
        Optional<UserEntity> userOptional = userRepository.findByEmailIgnoreCase("joe@dirt.com");
        assertThat(userOptional).isNotEmpty();
        UserEntity user = userOptional.get();

        assertThatThrownBy(() -> {
            UserResponse userResponse = userController.getUser(user.getId());
        }).isInstanceOf(AccessDeniedException.class);
    }
}
