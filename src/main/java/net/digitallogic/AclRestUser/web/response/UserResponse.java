package net.digitallogic.AclRestUser.web.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserResponse {

    private long id;
    private String email;
    private boolean accountExpired;
    private boolean accountDisabled;
    private boolean credentialsExpired;
    private boolean accountEnabled;

    @Builder.Default
    private List<String> roles = new ArrayList<>();
}
