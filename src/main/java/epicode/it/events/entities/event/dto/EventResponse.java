package epicode.it.events.entities.event.dto;

import epicode.it.events.entities.users.participant.dto.ParticipantResponse;
import epicode.it.events.entities.users.planner.dto.PlannerResponse;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class EventResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDate date;
    private String location;
    private int maxCapacity;
    private PlannerResponse planner;
    private List<ParticipantResponse> participants;
}
