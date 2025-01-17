package epicode.it.events.entities.users.EventUser;

import epicode.it.events.entities.users.participant.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventUserRepo extends JpaRepository<EventUser, Long> {

    public boolean existsByNameAndSurname(String name, String surname);
    public EventUser findByNameAndSurname(String name, String surname);
}
