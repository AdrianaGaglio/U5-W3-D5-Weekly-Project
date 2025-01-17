package epicode.it.events.entities.event;

import epicode.it.events.entities.event.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class EventController {
    private final EventSvc eventSvc;
    private final EventResponseMapper mapper;

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAll() {
        return ResponseEntity.ok(eventSvc.getAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<EventResponse>> getAllPageable(Pageable pageable) {
        return ResponseEntity.ok(eventSvc.getAllPageable(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toEventResponse(eventSvc.getById(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return new ResponseEntity<>(eventSvc.delete(id), HttpStatus.NO_CONTENT);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER')")
    public ResponseEntity<EventResponse> create(@RequestBody EventCreateRequest request) {
        return new ResponseEntity<>(eventSvc.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER')")
    public ResponseEntity<EventResponse> update(@PathVariable Long id, @RequestBody EventUpdateRequest request) {
        return new ResponseEntity<>(eventSvc.update(id, request), HttpStatus.OK);
    }

    @GetMapping("/by-planner/{plannerId}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER')")
    public ResponseEntity<List<EventResponse>> getAllByPlanner(@PathVariable Long plannerId) {
        return ResponseEntity.ok(eventSvc.findAllByPlanner(plannerId));
    }

    @PutMapping("/add-booking/{id}")
    public ResponseEntity<String> bookEvent(@PathVariable Long id, @RequestBody BookingRequest request) {
        return new ResponseEntity<>(eventSvc.bookEvent(id, request), HttpStatus.OK);
    }

    @PutMapping("/delete-booking/{id}")
    public ResponseEntity<String> undoBooking(@PathVariable Long id, @RequestBody BookingRequest request) {
        return new ResponseEntity<>(eventSvc.undoBooking(id, request), HttpStatus.OK);
    }

    @GetMapping("/by-participant/{participantId}")
    public ResponseEntity<List<EventResponse>> getAllByParticipant(@PathVariable Long participantId) {
        return ResponseEntity.ok(eventSvc.findAllByParticipant(participantId));
    }
}
