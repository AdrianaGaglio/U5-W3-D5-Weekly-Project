package epicode.it.events.entities.users.EventUser.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventUserCreateRequest {
    @NotNull(message = "Name is required")
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Surname is required")
    @NotBlank(message = "Surname is required")
    private String surname;

    private String image;

    private Long userId;

}
