package epicode.it.events.entities.users.participant;

import epicode.it.events.entities.users.participant.dto.ParticipantResponse;
import epicode.it.events.entities.users.participant.dto.ParticipantResponseMapper;
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
    private final ParticipantResponseMapper mapper;

    @GetMapping
    public ResponseEntity<List<ParticipantResponse>> getAll() {
        return ResponseEntity.ok(participantSvc.getAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<ParticipantResponse>> getAllPageable(Pageable pageable) {
        return ResponseEntity.ok(participantSvc.getAllPageable(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParticipantResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toParticipantResponse(participantSvc.getById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return new ResponseEntity<>(participantSvc.delete(id), HttpStatus.NO_CONTENT);
    }
}
