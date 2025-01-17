package epicode.it.events.entities.event;

import epicode.it.events.entities.users.planner.Planner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventRepo extends JpaRepository<Event, Long> {

    public Event findFirstByDateAndLocation(LocalDate date, String location);

    public boolean existsByDateAndLocation(LocalDate date, String location);

    public List<Event> findAllByPlanner(Planner planner);

    public boolean existsByPlannerAndDate(Planner planner, LocalDate date);


    @Query("SELECT e FROM Event e JOIN e.participants p WHERE p.id = :participantId ORDER BY e.date DESC")
    List<Event> findEventsByParticipantId(@Param("participantId") Long participantId);
}
