package epicode.it.events.entities.users.EventUser;

import epicode.it.events.entities.users.participant.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventUserRepo extends JpaRepository<EventUser, Long> {

    public boolean existsByNameAndSurname(String name, String surname);
    public EventUser findByNameAndSurname(String name, String surname);

    @Query("SELECT u FROM EventUser u WHERE u.appUser.username = :username")
    public EventUser findByUsername(@Param("username") String username);
}
