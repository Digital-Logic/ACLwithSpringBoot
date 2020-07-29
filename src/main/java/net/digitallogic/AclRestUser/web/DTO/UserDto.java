package net.digitallogic.AclRestUser.web.DTO;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long id;
    private String email;
    private String password;
    private boolean accountExpired;
    private boolean accountDisabled;
    private boolean credentialsExpired;
    private boolean accountEnabled;

    @Builder.Default
    private List<RoleDto> roles = new ArrayList<>();
}
