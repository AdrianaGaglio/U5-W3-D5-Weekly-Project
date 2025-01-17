package epicode.it.events.entities.event;

import epicode.it.events.entities.event.dto.EventCreateRequest;
import epicode.it.events.entities.event.dto.EventResponse;
import epicode.it.events.entities.event.dto.EventResponseMapper;
import epicode.it.events.entities.event.dto.EventUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
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
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return new ResponseEntity<>(eventSvc.delete(id), HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<EventResponse> create(@RequestBody EventCreateRequest request) {
        return new ResponseEntity<>(eventSvc.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> update(@PathVariable Long id, @RequestBody EventUpdateRequest request) {
        return new ResponseEntity<>(eventSvc.update(id, request), HttpStatus.OK);
    }

    @GetMapping("/by-planner/{plannerId}")
    public ResponseEntity<List<EventResponse>> getAllByPlanner(@PathVariable Long plannerId) {
        return ResponseEntity.ok(eventSvc.findAllByPlanner(plannerId));
    }
}
