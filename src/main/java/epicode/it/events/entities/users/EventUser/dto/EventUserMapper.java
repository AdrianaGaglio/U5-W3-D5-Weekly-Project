package epicode.it.events.entities.users.EventUser.dto;

import epicode.it.events.entities.users.EventUser.EventUser;
import epicode.it.events.entities.users.EventUser.EventUserSvc;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventUserMapper {
    private ModelMapper modelMapper = new ModelMapper();


    public EventUserResponse toEventUserResponse(EventUser u) {
        EventUserResponse response = modelMapper.map(u, EventUserResponse.class);
        response.setUsername(u.getAppUser().getUsername());
        response.setEmail(u.getAppUser().getEmail());
        return response;
    }

    public List<EventUserResponse> toEventUserResponseList(List<EventUser> users) {
        return users.stream().map(this::toEventUserResponse).toList();
    }
}
