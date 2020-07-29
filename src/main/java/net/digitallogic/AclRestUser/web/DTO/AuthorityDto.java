package net.digitallogic.AclRestUser.web.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@Builder
public class AuthorityDto {
    private long id;
    private String name;
}
