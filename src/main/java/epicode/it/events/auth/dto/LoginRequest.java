package epicode.it.events.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String email;

    @NotNull(message = "Password is required")
    @NotBlank(message = "Password is required")
    private String password;
}
