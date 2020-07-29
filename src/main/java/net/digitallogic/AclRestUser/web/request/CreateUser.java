package net.digitallogic.AclRestUser.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUser {
    @Size(max = 120, message = "{error.field.maxLength}")
    @NotNull(message = "{error.field.notNull}")
    private String email;

    @NotNull(message = "{error.field.notNull}")
    private String password;

    private boolean accountExpired;
    private boolean accountDisabled;
    private boolean credentialsExpired;
    private boolean accountEnabled;
}
