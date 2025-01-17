package epicode.it.events.entities.users.planner;

import epicode.it.events.entities.users.planner.dto.PlannerResponse;
import epicode.it.events.entities.users.planner.dto.PlannerResponseMapper;
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
    private final PlannerResponseMapper mapper;

    @GetMapping
    public ResponseEntity<List<PlannerResponse>> getAll() {
        return ResponseEntity.ok(plannerSvc.getAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<PlannerResponse>> getAllPageable(Pageable pageable) {
        return ResponseEntity.ok(plannerSvc.getAllPageable(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlannerResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toPlannerResponse(plannerSvc.getById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return new ResponseEntity<>(plannerSvc.delete(id), HttpStatus.NO_CONTENT);
    }
}
