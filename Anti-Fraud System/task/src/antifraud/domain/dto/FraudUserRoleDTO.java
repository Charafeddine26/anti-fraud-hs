package antifraud.domain.dto;

import antifraud.domain.enums.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FraudUserRoleDTO {

    private String username;
    private String role;
}
