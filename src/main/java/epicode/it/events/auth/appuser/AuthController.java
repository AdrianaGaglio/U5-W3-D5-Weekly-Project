package epicode.it.events.auth.appuser;


import epicode.it.events.auth.dto.AuthResponse;
import epicode.it.events.auth.dto.LoginRequest;
import epicode.it.events.auth.dto.RegisterRequest;
import epicode.it.events.exceptions.MissingInformationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AppUserSvc appUserSvc;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest, @RequestParam boolean isPlanner) {

        appUserSvc.registerUser(
                registerRequest,
                Set.of(isPlanner ? Role.ROLE_PLANNER : Role.ROLE_PARTICIPANT), // Assegna il ruolo di default
                isPlanner
        );

        return ResponseEntity.ok("Registrazione avvenuta con successo");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = appUserSvc.authenticateUser(loginRequest);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
