package epicode.it.events.utils.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmailRequest {
    @NotNull(message = "FROM is required")
    @NotBlank(message = "FROM is required")
    @Email(message = "Invalid email address for FROM field")
    private String from;

    @Email(message = "Invalid email address for REPLY TO field")
    private String replyTo;

    @Email(message = "Invalid email address for TO field")
    private String to;

    @Email(message = "Invalid email address for CC field")
    private String cc;

    @NotNull(message = "SUBJECT is required")
    @NotBlank(message = "SUBJECT is required")
    private String subject;

    @NotNull(message = "BODY is required")
    @NotBlank(message = "BODY is required")
    private String body;

}

