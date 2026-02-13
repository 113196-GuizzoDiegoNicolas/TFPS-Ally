package Ally.Scafolding.dtos.common.login;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordRequest {
    private String email;
    public String getEmail() { return email; }
}
