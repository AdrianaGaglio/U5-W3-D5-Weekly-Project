package epicode.it.events.auth.dto;

import epicode.it.events.auth.appuser.AppUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private IAppUserResponse user;
}
