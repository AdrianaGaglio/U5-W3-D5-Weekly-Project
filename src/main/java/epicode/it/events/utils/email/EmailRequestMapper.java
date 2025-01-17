package epicode.it.events.utils.email;

import epicode.it.events.entities.event.Event;
import epicode.it.events.entities.event.dto.EventResponse;
import epicode.it.events.entities.users.EventUser.EventUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailRequestMapper {

    public EmailRequest toEmailRequestFromNewEvent(EventResponse e, String from) {
        EmailRequest request = new EmailRequest();
        request.setTo(e.getPlanner().getEmail());
        request.setFrom(from);
        request.setSubject("New event created successfully");
        request.setBody("New event created successfully: " + e.toString());
        return request;
    }

    public EmailRequest toEmailRequestFromUpdateEvent(EventResponse e, String from) {
        EmailRequest request = new EmailRequest();
        request.setTo(e.getPlanner().getEmail());
        request.setFrom(from);
        request.setSubject("Event updated successfully");
        request.setBody("Event updated successfully: " + e.toString());
        return request;
    }

    public EmailRequest toEmailRequestFromBooking(Event e, EventUser user, String from, String subject) {
        EmailRequest request = new EmailRequest();
        request.setTo(user.getAppUser().getEmail());
        request.setFrom(from);
        request.setSubject(subject);
        request.setBody(subject + ": " + e.toString());
        return request;
    }
}
