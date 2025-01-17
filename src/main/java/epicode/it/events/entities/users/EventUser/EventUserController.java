package epicode.it.events.entities.users.EventUser;

import epicode.it.events.entities.users.EventUser.dto.EventUserCreateRequest;
import epicode.it.events.entities.users.EventUser.dto.EventUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event-users")
@RequiredArgsConstructor
public class EventUserController {
    private final EventUserSvc eventUserSvc;

    @PostMapping
    public ResponseEntity<EventUserResponse> create(@RequestBody EventUserCreateRequest request, @RequestParam boolean isPlanner) {
        return new ResponseEntity<>(eventUserSvc.create(request, isPlanner), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || isAuthenticated()")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return new ResponseEntity<>(eventUserSvc.delete(id), HttpStatus.NO_CONTENT);
    }


}
