package epicode.it.events.entities.event;


import epicode.it.events.entities.event.dto.*;
import epicode.it.events.entities.users.EventUser.EventUser;
import epicode.it.events.entities.users.participant.Participant;
import epicode.it.events.entities.users.participant.ParticipantSvc;
import epicode.it.events.entities.users.planner.Planner;
import epicode.it.events.entities.users.planner.PlannerRepo;
import epicode.it.events.exceptions.BookingExistsException;
import epicode.it.events.utils.email.EmailRequest;
import epicode.it.events.utils.email.EmailRequestMapper;
import epicode.it.events.utils.email.EmailSvc;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class EventSvc {
    private final EventRepo eventRepo;
    private final PlannerRepo plannerRepo;
    private final EventResponseMapper mapper;
    private final ParticipantSvc participantSvc;
    private final EmailSvc email;
    private final EmailRequestMapper emailMapper;

    public List<EventResponse> getAll() {

        return mapper.toEventResponseList(eventRepo.findAll());
    }

    public Page<epicode.it.events.entities.event.dto.EventResponse> getAllPageable(Pageable pageable) {

        Page<Event> pagedEvents = eventRepo.findAll(pageable);
        Page<epicode.it.events.entities.event.dto.EventResponse> response = pagedEvents.map(e -> {
            epicode.it.events.entities.event.dto.EventResponse eventResponse = mapper.toEventResponse(e);
            return eventResponse;
        });
        return response;
    }

    public Event getById(Long id) {
        return eventRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Event not found"));
    }

    public int count() {
        return (int) eventRepo.count();
    }

    public String delete(Long id) {
        Event e = getById(id);
        eventRepo.delete(e);
        return "Event deleted successfully";
    }

    public String delete(Event e) {
        Event foundEvent = getById(e.getId());
        eventRepo.delete(foundEvent);
        return "Event deleted successfully";
    }

    public Event findByDateAndLocation(LocalDate date, String location) {
        return eventRepo.findFirstByDateAndLocation(date, location);
    }

    public List<EventResponse> findAllByPlanner(Long plannerId) {
        Planner p = plannerRepo.findById(plannerId).orElseThrow(() -> new EntityNotFoundException("Planner not found"));
        return mapper.toEventResponseList(eventRepo.findAllByPlanner(p));
    }

    @Transactional
    public EventResponse create(@Valid EventCreateRequest request) {
        Planner p = plannerRepo.findById(request.getPlannerId()).orElseThrow(() -> new EntityNotFoundException("Planner not found"));

        if (eventRepo.existsByDateAndLocation(request.getDate(), request.getLocation()))
            throw new EntityExistsException("Event already exists");

        if (eventRepo.existsByPlannerAndDate(p, request.getDate()))
            throw new EntityExistsException("Planner already has an event for this date");

        Event event = new Event();
        BeanUtils.copyProperties(request, event);
        event.setPlanner(p);
        EventResponse response = mapper.toEventResponse(eventRepo.save(event));

        EmailRequest emailRequest = emailMapper.toEmailRequestFromNewEvent(response, "events@epicode.it");
        email.sendEmail(emailRequest);
        return response;
    }

    public EventResponse update(Long id, @Valid EventUpdateRequest request) {
        Event e = getById(id);
        BeanUtils.copyProperties(request, e);
        EventResponse response = mapper.toEventResponse(eventRepo.save(e));
        EmailRequest emailRequest = emailMapper.toEmailRequestFromUpdateEvent(response, "events@epicode.it");
        email.sendEmail(emailRequest);
        return response;
    }

    public String bookEvent(Long id, @Valid BookingRequest request) {
        Event e = getById(id);
        EventUser user = participantSvc.getById(request.getUserId());
        if (e.getParticipants().contains(user)) throw new BookingExistsException("User already booked this event");
        if (e.getMaxCapacity() == e.getParticipants().size()) throw new BookingExistsException("Event is full");
        e.getParticipants().add((Participant) user);
        eventRepo.save(e);
        String subject = "Event booked successfully";
        EmailRequest emailRequest = emailMapper.toEmailRequestFromBooking(e, user, "events@epicode.it", subject);
        email.sendEmail(emailRequest);
        return subject;
    }

    public String undoBooking(Long id, @Valid BookingRequest request) {
        Event e = getById(id);
        EventUser user = participantSvc.getById(request.getUserId());
        e.getParticipants().remove(user);
        eventRepo.save(e);
        String subject = "Booking cancelled successfully";
        EmailRequest emailRequest = emailMapper.toEmailRequestFromBooking(e, user, "events@epicode.it", subject);
        email.sendEmail(emailRequest);
        return subject;
    }

    public List<EventResponse> findAllByParticipant(Long participantId) {
        return mapper.toEventResponseList(eventRepo.findEventsByParticipantId(participantId));
    }
}
