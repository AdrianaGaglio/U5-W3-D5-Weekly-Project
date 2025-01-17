package epicode.it.events.entities.users.planner;

import com.github.javafaker.Faker;
import epicode.it.events.auth.appuser.AppUserSvc;
import epicode.it.events.auth.appuser.Role;
import epicode.it.events.auth.dto.RegisterRequest;
import epicode.it.events.entities.users.EventUser.EventUser;
import epicode.it.events.entities.users.EventUser.EventUserSvc;
import epicode.it.events.entities.users.EventUser.dto.EventUserCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class PlannerRunner implements ApplicationRunner {
    private final EventUserSvc eventUserSvc;
    private final PlannerSvc plannerSvc;
    private final AppUserSvc appUserSvc;
    private final Faker faker;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (plannerSvc.count() == 0) {

            for (int i = 0; i < 10; i++) {
                EventUserCreateRequest user = new EventUserCreateRequest();
                user.setName(faker.name().firstName());
                user.setSurname(faker.name().lastName());

                String surname = user.getSurname().toLowerCase();
                if (surname.contains(" ") || surname.contains("'")) {
                    surname = surname.replace(" ", "");
                    surname = surname.replace("'", "");
                }

                String username = user.getName().toLowerCase() + surname.charAt(0);
                String email = user.getName().toLowerCase() + "." + surname + "@mail.com";

                RegisterRequest request = new RegisterRequest(username, email, "password", user);

                try {

                    appUserSvc.registerUser(request, Set.of(Role.ROLE_PLANNER), true);
                } catch (RuntimeException e) {
                    System.out.println("===> " + request);
                    System.out.println(e.getMessage());
                }
            }

        }
    }
}
