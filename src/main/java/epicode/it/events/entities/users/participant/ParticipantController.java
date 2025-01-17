package epicode.it.events.entities.users.participant;

import epicode.it.events.entities.users.participant.dto.ParticipantResponse;
import epicode.it.events.entities.users.participant.dto.ParticipantResponseMapper;
import epicode.it.events.entities.users.planner.Planner;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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

@RestController
@RequestMapping("/api/participants")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class ParticipantController {
    private final ParticipantSvc participantSvc;
    private final ParticipantResponseMapper mapper;

    @GetMapping
    public ResponseEntity<List<ParticipantResponse>> getAll() {
        return ResponseEntity.ok(participantSvc.getAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<ParticipantResponse>> getAllPageable(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(participantSvc.getAllPageable(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParticipantResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toParticipantResponse(participantSvc.getById(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('PARTICIPANT')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        StringBuilder roles = new StringBuilder();
        authentication.getAuthorities().forEach(authority -> roles.append(authority.getAuthority()).append(""));
        if (!roles.toString().contains("ROLE_ADMIN")) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Participant p = participantSvc.getById(id);
            if (!p.getAppUser().getUsername().equals(username)) {
                throw new AccessDeniedException("You cannot access this event");
            }
        }

        return new ResponseEntity<>(participantSvc.delete(id), HttpStatus.NO_CONTENT);
    }
}
