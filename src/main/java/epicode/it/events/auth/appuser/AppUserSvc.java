package epicode.it.events.auth.appuser;


import epicode.it.events.auth.configurations.PwdEncoder;
import epicode.it.events.auth.dto.LoginRequest;
import epicode.it.events.auth.dto.RegisterRequest;
import epicode.it.events.auth.jwt.JwtTokenUtil;
import epicode.it.events.entities.users.EventUser.EventUserSvc;
import epicode.it.events.entities.users.EventUser.dto.EventUserCreateRequest;
import epicode.it.events.exceptions.MissingInformationException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor // Genera automaticamente un costruttore con tutti i campi final, riducendo il boilerplate
@Validated
public class AppUserSvc {

    private final AppUserRepo appUserRepo; // Repository per gestire le operazioni di persistenza per AppUser
    private final PwdEncoder encoder; // Utilità per codificare le password
    private final AuthenticationManager authenticationManager; // Gestisce il processo di autenticazione
    private final JwtTokenUtil jwtTokenUtil; // Utilità per generare e gestire token JWT
    private final EventUserSvc eventUserSvc;

    //    /**
//     * Registra un nuovo utente nel sistema.
//     * @param username Il nome utente dell'utente.
//     * @param password La password dell'utente.
//     * @param roles I ruoli assegnati all'utente.
//     * @return L'entità salvata AppUser.
//     */
    @Transactional
    public AppUser registerUser(@Valid RegisterRequest request, Set<Role> roles, boolean isPlanner) {
        // Controlla se l'username esiste già nel database.
        if (appUserRepo.existsByUsername(request.getUsername())) {
            throw new EntityExistsException("Username not available"); // Lancia un'eccezione se l'username è già in uso
        }

        if (appUserRepo.existsByEmail(request.getEmail())) {
            throw new EntityExistsException("Email already registered"); // Lancia un'eccezione se l'username è già in uso
        }

        // Crea una nuova istanza di AppUser e imposta i suoi campi.
        AppUser appUser = new AppUser();
        appUser.setUsername(request.getUsername());
        appUser.setEmail(request.getEmail());
        // Codifica la password utilizzando il PasswordEncoder.
        appUser.setPassword(encoder.passwordEncoder().encode(request.getPassword()));
        appUser.setRoles(roles); // Imposta i ruoli per l'utente.

        appUser=appUserRepo.save(appUser);

        if (request.getEventUserCreateRequest() != null) {
            EventUserCreateRequest userRequest = request.getEventUserCreateRequest();
            BeanUtils.copyProperties(request.getEventUserCreateRequest(), userRequest);
            userRequest.setUserId(appUser.getId());
            eventUserSvc.create(userRequest, isPlanner);
        }

        // Salva l'utente nel database e restituisce l'oggetto salvato.
        return appUser;


    }

    //    /**
//     * Trova un utente in base all'username.
//     * @param username Il nome utente da cercare.
//     * @return Un Optional che contiene l'utente, se trovato.
//     */
    public AppUser findByUsername(String username) {
        // Cerca l'utente nel database in base all'username.
        return appUserRepo.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public AppUser findByEmail(String email) {
        // Cerca l'utente nel database in base all'username.
        return appUserRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }


    //    /**
//     * Autentica un utente e genera un token JWT se l'autenticazione ha successo.
//     * @param username Il nome utente.
//     * @param password La password.
//     * @return Un token JWT valido.
//     */
    public String authenticateUser(@Valid LoginRequest request) {
        try {

            boolean hasUsername = request.getUsername() != null && !request.getUsername().isEmpty();
            if (!hasUsername) {
                AppUser user = findByEmail(request.getEmail());
                request.setUsername(user.getUsername());
            }

            // Crea un token di autenticazione e prova ad autenticare l'utente.
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // Recupera i dettagli dell'utente autenticato.
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // Genera un token JWT per l'utente autenticato.
            return jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            // Lancia un'eccezione di sicurezza se l'autenticazione fallisce.
            throw new SecurityException("Invalid credentials", e);
        }
    }

    public AppUser findByUsernameOrEmail(String usernameOrEmail) {
        return appUserRepo.findByUsernameOrEmail(usernameOrEmail).orElse(null);
    }

    //    /**
//     * Carica un utente dal database utilizzando l'username o l'email.
//     * @param username Il nome utente da cercare.
//     * @return L'entità AppUser corrispondente.
//     */
    public AppUser loadUserByUsername(String usernameOrEmail) {
        // Cerca l'utente nel database, lanciando un'eccezione se non viene trovato.
        AppUser appUser = appUserRepo.findByUsernameOrEmail(usernameOrEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + usernameOrEmail));

        // Restituisce l'utente trovato.
        return appUser;
    }
}
