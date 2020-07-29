package net.digitallogic.AclRestUser.services;

import net.digitallogic.AclRestUser.web.DTO.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    public UserDto createUser(UserDto userData);
    public UserDto getUser(long id);
    public List<UserDto> getAllUsers();
}
