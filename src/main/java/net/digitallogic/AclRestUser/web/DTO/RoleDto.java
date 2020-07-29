package net.digitallogic.AclRestUser.web.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(of = "id")
@Builder
public class RoleDto {
    private long id;
    private String name;

    @Builder.Default
    private List<AuthorityDto> authorities = new ArrayList<>();
}
