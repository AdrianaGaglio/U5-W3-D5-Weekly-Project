package epicode.it.events.entities.users.participant;

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
@RequestMapping("/api/participants")
@RequiredArgsConstructor
public class ParticipantController {
    private final ParticipantSvc participantSvc;
    private final EventUserSvc eventUserSvc;

    @GetMapping
    public ResponseEntity<List<Participant>> getAll() {
        return ResponseEntity.ok(participantSvc.getAll());
    }

    @GetMapping
    public ResponseEntity<Page<Participant>> getAllPageable(Pageable pageable) {
        return ResponseEntity.ok(participantSvc.getAllPageable(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Participant> getById(@PathVariable Long id) {
        return ResponseEntity.ok(participantSvc.getById(id));
    }

    @PostMapping
    public ResponseEntity<Participant> create(@RequestBody EventUserCreateRequest request) {
        return new ResponseEntity<>((Participant) eventUserSvc.create(request, false), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Participant> update(@PathVariable Long id, @RequestBody EventUserUpdateRequest request) {
        return new ResponseEntity<>((Participant) eventUserSvc.update(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return new ResponseEntity<>(participantSvc.delete(id), HttpStatus.NO_CONTENT);
    }
}
