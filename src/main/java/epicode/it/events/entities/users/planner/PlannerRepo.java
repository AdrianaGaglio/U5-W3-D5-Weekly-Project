package epicode.it.events.entities.users.planner;

import epicode.it.events.entities.users.participant.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlannerRepo extends JpaRepository<Planner, Long> {

    public boolean existsByNameAndSurname(String name, String surname);
    public Planner findByNameAndSurname(String name, String surname);
}
