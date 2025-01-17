package epicode.it.events.entities.users.EventUser.dto;

import lombok.Data;

@Data
public class EventUserResponse {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String surname;
}
