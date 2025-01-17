package epicode.it.events.auth.dto;

import epicode.it.events.entities.users.EventUser.dto.EventUserCreateRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotNull(message = "Username is required")
    @NotBlank(message = "Username is required")
    private String username;

    @NotNull(message = "Username is required")
    @NotBlank(message = "Username is required")
    @Email(message = "Invalid email address")
    private String email;

    @NotNull(message = "Password is required")
    @NotBlank(message = "Password is required")
    private String password;

    private EventUserCreateRequest eventUserCreateRequest;

    public RegisterRequest() {}

    public RegisterRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public RegisterRequest(String username, String email, String password, EventUserCreateRequest eventUserCreateRequest) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.eventUserCreateRequest = eventUserCreateRequest;
    }
}
