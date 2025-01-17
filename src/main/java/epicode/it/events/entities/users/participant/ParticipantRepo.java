package epicode.it.events.entities.users.participant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParticipantRepo extends JpaRepository<Participant, Long> {

    public boolean existsByNameAndSurname(String name, String surname);
    public Participant findByNameAndSurname(String name, String surname);

    @Query("SELECT p FROM Event e JOIN e.participants p WHERE e.id = :eventId")
    List<Participant> findByEventId(@Param("eventId") Long eventId);

}
