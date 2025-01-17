package epicode.it.events.entities.event;

import epicode.it.events.entities.event.dto.EventCreateRequest;
import epicode.it.events.entities.event.dto.EventUpdateRequest;
import epicode.it.events.entities.users.planner.Planner;
import epicode.it.events.entities.users.planner.PlannerRepo;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
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

    public List<Event> getAll() {
        return eventRepo.findAll();
    }

    public Page<Event> getAllPaged(Pageable pageable) {
        return eventRepo.findAll(pageable);
    }

    public Page<Event> getAllPageable(Pageable pageable) {
        return eventRepo.findAll(pageable);
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

    public List<Event> findAllByPlanner(Long plannerId) {
        Planner p = plannerRepo.findById(plannerId).orElseThrow(() -> new EntityNotFoundException("Planner not found"));
        return eventRepo.findAllByPlanner(p);
    }

    public Event create(@Valid EventCreateRequest request) {
        if (eventRepo.existsByDateAndLocation(request.getDate(), request.getLocation()))
            throw new EntityExistsException("Event already exists");
        Event event = new Event();
        BeanUtils.copyProperties(request, event);
        event.setPlanner(plannerRepo.findById(request.getPlannerId()).orElseThrow(() -> new EntityNotFoundException("Planner not found")));
        return eventRepo.save(event);
    }

    public Event update(Long id, @Valid EventUpdateRequest request) {
        Event e = getById(id);
        BeanUtils.copyProperties(request, e);
        return eventRepo.save(e);
    }
}
