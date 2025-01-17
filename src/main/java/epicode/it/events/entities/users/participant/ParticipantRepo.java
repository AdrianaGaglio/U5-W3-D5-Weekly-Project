package epicode.it.events.entities.users.participant;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepo extends JpaRepository<Participant, Long> {

    public boolean existsByNameAndSurname(String name, String surname);
    public Participant findByNameAndSurname(String name, String surname);

    public List<Participant> findByEventId(Long eventId);
}
