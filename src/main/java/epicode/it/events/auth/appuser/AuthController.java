package epicode.it.events.auth.appuser;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import epicode.it.events.auth.dto.AuthResponse;
import epicode.it.events.auth.dto.LoginRequest;
import epicode.it.events.auth.dto.RegisterRequest;
import epicode.it.events.exceptions.MissingInformationException;
import epicode.it.events.utils.upload.UploadSvc;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AppUserSvc appUserSvc;
    private final UploadSvc uploadSvc;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest, @RequestParam boolean isPlanner) {

        appUserSvc.registerUser(
                registerRequest,
                Set.of(isPlanner ? Role.ROLE_PLANNER : Role.ROLE_PARTICIPANT), // Assegna il ruolo di default
                isPlanner
        );

        return ResponseEntity.ok("Registrazione avvenuta con successo");
    }

    @PostMapping(path = "/register-with-image", consumes = {"multipart/form-data"})
    public ResponseEntity<String> register(@RequestPart(value = "registerRequest", required = true) String registerRequest,
                                           @RequestPart(value = "file", required = false) MultipartFile file,
                                           @RequestParam boolean isPlanner) {

        ObjectMapper mapper = new ObjectMapper();
        RegisterRequest request;

        try {
            request = mapper.readValue(registerRequest, RegisterRequest.class);
            if (file != null) {
                request.getEventUserCreateRequest().setImage(uploadSvc.uploadFile(file));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        appUserSvc.registerUser(
                request,
                Set.of(isPlanner ? Role.ROLE_PLANNER : Role.ROLE_PARTICIPANT), // Assegna il ruolo di default
                isPlanner
        );

        return ResponseEntity.ok("Registrazione avvenuta con successo");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(appUserSvc.authenticateUser(loginRequest));
    }
}
