package epicode.it.events.entities.users.participant;

import epicode.it.events.entities.users.EventUser.dto.EventUserCreateRequest;
import epicode.it.events.entities.users.EventUser.dto.EventUserUpdateRequest;
import epicode.it.events.entities.users.utils.Utils;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class ParticipantSvc {
    private final ParticipantRepo participantRepo;

    public List<Participant> getAll() {

        return participantRepo.findAll();
    }

    public Page<Participant> getAllPageable(Pageable pageable) {

        return participantRepo.findAll(pageable);
    }

    public Participant getById(Long id) {
        return participantRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Participant not found"));
    }

    public int count() {
        return (int) participantRepo.count();
    }

    public String delete(Long id) {
        Participant e = getById(id);
        participantRepo.delete(e);
        return "Participant deleted successfully";
    }

    public String delete(Participant e) {
        Participant foundParticipant = getById(e.getId());
        participantRepo.delete(foundParticipant);
        return "Participant deleted successfully";
    }

    public List<Participant> findByEventId(Long id) {
        return participantRepo.findByEventId(id);
    }
}
