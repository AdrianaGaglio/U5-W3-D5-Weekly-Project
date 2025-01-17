package epicode.it.events.entities.event;

import epicode.it.events.entities.users.participant.Participant;
import epicode.it.events.entities.users.planner.Planner;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private  Long id;

    private String title;

    private String description;

    private LocalDate date;

    private String location;

    @Column(name = "max_capacity")
    private int maxCapacity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "planner_id")
    private Planner planner;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "event_participants", joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "participant_id"))
    private List<Participant> participants = new ArrayList<>();

}