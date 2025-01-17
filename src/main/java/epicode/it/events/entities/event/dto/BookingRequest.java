package epicode.it.events.entities.event.dto;

import lombok.Data;

@Data
public class BookingRequest {
    private Long eventId;
    private Long userId;
}
