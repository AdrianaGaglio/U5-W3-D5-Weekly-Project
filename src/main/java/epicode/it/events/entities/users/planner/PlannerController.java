package epicode.it.events.entities.users.planner;

import epicode.it.events.entities.users.EventUser.EventUser;
import epicode.it.events.entities.users.EventUser.EventUserSvc;
import epicode.it.events.entities.users.EventUser.dto.EventUserCreateRequest;
import epicode.it.events.entities.users.EventUser.dto.EventUserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planners")
@RequiredArgsConstructor
public class PlannerController {
    private final PlannerSvc plannerSvc;
    private final EventUserSvc eventUserSvc;

    @GetMapping
    public ResponseEntity<List<Planner>> getAll() {
        return ResponseEntity.ok(plannerSvc.getAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<Planner>> getAllPageable(Pageable pageable) {
        return ResponseEntity.ok(plannerSvc.getAllPageable(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Planner> getById(@PathVariable Long id) {
        return ResponseEntity.ok(plannerSvc.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return new ResponseEntity<>(plannerSvc.delete(id), HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<Planner> create(@RequestBody EventUserCreateRequest request) {
        return new ResponseEntity<>((Planner) eventUserSvc.create(request, true), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Planner> update(@PathVariable Long id, @RequestBody EventUserUpdateRequest
            request) {
        return new ResponseEntity<>((Planner) eventUserSvc.update(id, request), HttpStatus.OK);
    }
}
