package net.digitallogic.AclRestUser.web.controller;

import net.digitallogic.AclRestUser.mapper.UserMapper;
import net.digitallogic.AclRestUser.services.UserService;
import net.digitallogic.AclRestUser.web.Routes;
import net.digitallogic.AclRestUser.web.request.CreateUser;
import net.digitallogic.AclRestUser.web.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = Routes.USER_ROUTE)
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;

    @Autowired
    public UserController(UserMapper userMapper, UserService userService) {
        this.userMapper = userMapper;
        this.userService = userService;
    }

    /**
     * Get One User
     * @param id
     * @return
     */
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PostAuthorize("hasAnyAuthority('ADMIN_USER', 'READ_USER') or " +
            "hasPermission(#id, 'net.digitallogic.AclRestUser.persistence.entity.UserEntity', 'read')")
    public UserResponse getUser(@PathVariable long id) {
        return userMapper.toResponse(
            userService.getUser(id)
        );
    }

    /**
     * Get all users
     * @return
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN_USER') or hasAuthority('READ_USER')")
    public List<UserResponse> getUsers() {
        return userMapper.toResponse(
            userService.getAllUsers()
        );
    }

    /**
     * Create a user
     * @param userData
     * @return
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody CreateUser userData) {
        return userMapper.toResponse(
            userService.createUser(userMapper.toDto(userData))
        );
    }
}
