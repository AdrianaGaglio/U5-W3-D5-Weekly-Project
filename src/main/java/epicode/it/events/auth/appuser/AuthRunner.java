package epicode.it.events.auth.appuser;

import epicode.it.events.auth.configurations.PwdEncoder;
import epicode.it.events.auth.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class AuthRunner implements ApplicationRunner {

    @Autowired
    private AppUserSvc appUserSvc;

    @Autowired
    private PwdEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Creazione dell'utente admin se non esiste
        AppUser adminUser = appUserSvc.findByUsernameOrEmail("admin");
        if (adminUser == null) {
            RegisterRequest request = new RegisterRequest("admin", "admin@mail.com", "adminpwd");
            appUserSvc.registerUser(request, Set.of(Role.ROLE_ADMIN), false);
        }
    }
}
