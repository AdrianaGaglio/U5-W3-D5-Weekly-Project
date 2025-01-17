package epicode.it.events.entities.users.participant.dto;

import epicode.it.events.entities.users.participant.Participant;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ParticipantResponseMapper {
    private ModelMapper modelMapper = new ModelMapper();


    public ParticipantResponse toParticipantResponse(Participant u) {
        ParticipantResponse response = modelMapper.map(u, ParticipantResponse.class);
        response.setUsername(u.getAppUser().getUsername());
        response.setEmail(u.getAppUser().getEmail());
        return response;
    }

    public List<ParticipantResponse> toParticipantResponseList(List<Participant> users) {
        return users.stream().map(this::toParticipantResponse).toList();
    }
}
