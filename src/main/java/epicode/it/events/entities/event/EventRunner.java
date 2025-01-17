package epicode.it.events.entities.event;

import com.github.javafaker.Faker;
import epicode.it.events.entities.event.dto.EventCreateRequest;
import epicode.it.events.entities.users.planner.dto.PlannerResponse;
import epicode.it.events.entities.users.planner.PlannerSvc;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Order(2)
public class EventRunner implements ApplicationRunner {
    private final EventSvc eventSvc;
    private final PlannerSvc plannerSvc;
    private final Faker faker;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (eventSvc.count() == 0) {

            for (int i = 0; i < 30; i++) {
                EventCreateRequest request = new EventCreateRequest();
                request.setTitle(faker.lorem().fixedString(10));
                request.setDescription(faker.lorem().sentence(10));
                request.setLocation(faker.address().cityName());
                request.setDate(faker.date().future(365, TimeUnit.DAYS).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
                request.setMaxCapacity(faker.number().numberBetween(10, 1000));

                List<PlannerResponse> planners = plannerSvc.getAll();
                request.setPlannerId(planners.get(faker.number().numberBetween(0, planners.size() - 1)).getId());

                try {
                    eventSvc.create(request);
                } catch (RuntimeException e) {
                    System.out.println("===> " + request);
                    System.out.println(e.getMessage());
                }

            }

        }

    }
}
