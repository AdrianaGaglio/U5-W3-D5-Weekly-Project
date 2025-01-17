package epicode.it.events.entities.users.EventUser;

import epicode.it.events.entities.users.EventUser.dto.EventUserCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event-users")
@RequiredArgsConstructor
public class EventUserController {
    private final EventUserSvc eventUserSvc;

    @PostMapping
    public ResponseEntity<EventUser> create(@RequestBody EventUserCreateRequest request, @RequestParam boolean isPlanner) {
        return new ResponseEntity<>(eventUserSvc.create(request, isPlanner), HttpStatus.CREATED);
    }


}
