package epicode.it.events.entities.event;


import epicode.it.events.entities.event.dto.EventCreateRequest;
import epicode.it.events.entities.event.dto.EventResponse;
import epicode.it.events.entities.event.dto.EventResponseMapper;
import epicode.it.events.entities.event.dto.EventUpdateRequest;
import epicode.it.events.entities.users.planner.Planner;
import epicode.it.events.entities.users.planner.PlannerRepo;
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
        return mapper.toEventResponse(eventRepo.save(event));
    }

    public EventResponse update(Long id, @Valid EventUpdateRequest request) {
        Event e = getById(id);
        BeanUtils.copyProperties(request, e);
        return mapper.toEventResponse(eventRepo.save(e));
    }
}
