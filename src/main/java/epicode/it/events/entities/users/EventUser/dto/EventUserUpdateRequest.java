package epicode.it.events.entities.users.EventUser.dto;

import lombok.Data;

@Data
public class EventUserUpdateRequest {
    private String name;

    private String surname;

    private String image;
}
