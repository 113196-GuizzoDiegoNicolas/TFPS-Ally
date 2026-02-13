package Ally.Scafolding.dtos.common.login;

public record ResetPasswordRequest(String token, String newPassword) {
}
