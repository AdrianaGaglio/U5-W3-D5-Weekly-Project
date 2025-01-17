package epicode.it.events.entities.event.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EventUpdateRequest {
    private String title;
    private String description;

    @FutureOrPresent(message = "Date cannot be in the past")
    private LocalDate date;

    private String location;

    @Min(value = 10, message = "Max capacity must be at least 10")
    private int maxCapacity;
    private Long plannerId;
}
