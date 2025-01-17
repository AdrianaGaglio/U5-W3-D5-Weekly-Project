package epicode.it.events.entities.event;

import epicode.it.events.entities.users.planner.Planner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventRepo extends JpaRepository<Event, Long> {

    public Event findFirstByDateAndLocation(LocalDate date, String location);

    public boolean existsByDateAndLocation(LocalDate date, String location);

    public List<Event> findAllByPlanner(Planner planner);

    public boolean existsByPlannerAndDate(Planner planner, LocalDate date);
}
