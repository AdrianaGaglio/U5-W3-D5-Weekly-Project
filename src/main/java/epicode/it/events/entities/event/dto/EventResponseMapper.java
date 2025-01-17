package epicode.it.events.entities.event.dto;

import epicode.it.events.entities.event.Event;
import epicode.it.events.entities.users.participant.dto.ParticipantResponseMapper;
import epicode.it.events.entities.users.planner.dto.PlannerResponseMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventResponseMapper {
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private PlannerResponseMapper plannerMapper;

    @Autowired
    private ParticipantResponseMapper participantMapper;

    public epicode.it.events.entities.event.dto.EventResponse toEventResponse(Event e) {
        epicode.it.events.entities.event.dto.EventResponse response = modelMapper.map(e, epicode.it.events.entities.event.dto.EventResponse.class);
        response.setPlanner(plannerMapper.toPlannerResponse(e.getPlanner()));
        response.setParticipants(participantMapper.toParticipantResponseList(e.getParticipants()));
        return response;
    }

    public List<epicode.it.events.entities.event.dto.EventResponse> toEventResponseList(List<Event> events) {
        return events.stream().map(this::toEventResponse).toList();
    }
}
