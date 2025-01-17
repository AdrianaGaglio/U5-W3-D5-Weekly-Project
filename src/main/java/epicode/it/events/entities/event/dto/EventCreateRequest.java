package epicode.it.events.entities.event.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EventCreateRequest {
    @NotNull(message = "Title is required")
    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Description is required")
    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Date cannot be in the past")
    private LocalDate date;

    @NotNull(message = "Location is required")
    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Max capacity is required")
    @Min(value = 10, message = "Max capacity must be at least 10")
    private int maxCapacity;

    @NotNull(message = "Planner ID is required")
    private Long plannerId;
}
