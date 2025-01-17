package epicode.it.events.entities.users.planner;

import epicode.it.events.entities.users.planner.dto.PlannerResponse;
import epicode.it.events.entities.users.planner.dto.PlannerResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planners")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class PlannerController {
    private final PlannerSvc plannerSvc;
    private final PlannerResponseMapper mapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER')")
    public ResponseEntity<List<PlannerResponse>> getAll() {
        return ResponseEntity.ok(plannerSvc.getAll());
    }

    @GetMapping("/paged")
    @PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER')")
    public ResponseEntity<Page<PlannerResponse>> getAllPageable(Pageable pageable) {
        return ResponseEntity.ok(plannerSvc.getAllPageable(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('PLANNER')")
    public ResponseEntity<PlannerResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toPlannerResponse(plannerSvc.getById(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return new ResponseEntity<>(plannerSvc.delete(id), HttpStatus.NO_CONTENT);
    }
}
