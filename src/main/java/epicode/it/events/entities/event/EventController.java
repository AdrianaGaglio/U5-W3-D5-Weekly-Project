package epicode.it.events.entities.event;

import epicode.it.events.auth.jwt.JwtRequestFilter;
import epicode.it.events.entities.event.dto.*;
import epicode.it.events.entities.users.planner.Planner;
import epicode.it.events.entities.users.planner.PlannerSvc;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static epicode.it.events.auth.jwt.JwtRequestFilter.extractRoles;
import static epicode.it.events.auth.jwt.JwtRequestFilter.extractUsername;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class EventController {
    private final EventSvc eventSvc;
    private final EventResponseMapper mapper;
    private final PlannerSvc plannerSvc;

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAll() {
        return ResponseEntity.ok(eventSvc.getAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<EventResponse>> getAllPageable(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(eventSvc.getAllPageable(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toEventResponse(eventSvc.getById(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        StringBuilder roles = new StringBuilder();
        authentication.getAuthorities().forEach(authority -> roles.append(authority.getAuthority()).append(""));
        if (!roles.toString().contains("ROLE_ADMIN")) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Event event = eventSvc.getById(id);
            if (!event.getPlanner().getAppUser().getUsername().equals(username)) {
                throw new AccessDeniedException("You cannot access this event");
            }
        }

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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        StringBuilder roles = new StringBuilder();
        authentication.getAuthorities().forEach(authority -> roles.append(authority.getAuthority()).append(""));
        if (!roles.toString().contains("ROLE_ADMIN")) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Event event = eventSvc.getById(id);
            if (!event.getPlanner().getAppUser().getUsername().equals(username)) {
                throw new AccessDeniedException("You cannot access this event");
            }
        }

        return new ResponseEntity<>(eventSvc.update(id, request), HttpStatus.OK);
    }

    @GetMapping("/by-planner/{plannerId}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER')")
    public ResponseEntity<List<EventResponse>> getAllByPlanner(@PathVariable Long plannerId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        StringBuilder roles = new StringBuilder();
        authentication.getAuthorities().forEach(authority -> roles.append(authority.getAuthority()).append(""));
        if (!roles.toString().contains("ROLE_ADMIN")) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Planner p = plannerSvc.getById(plannerId);
            if (!p.getAppUser().getUsername().equals(username)) {
                throw new AccessDeniedException("You cannot access this event");
            }
        }

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
