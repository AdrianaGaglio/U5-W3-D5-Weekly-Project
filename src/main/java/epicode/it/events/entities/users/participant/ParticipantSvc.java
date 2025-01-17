package epicode.it.events.entities.users.participant;

import epicode.it.events.entities.users.participant.dto.ParticipantResponse;
import epicode.it.events.entities.users.participant.dto.ParticipantResponseMapper;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

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
    private final ParticipantResponseMapper mapper;

    public List<ParticipantResponse> getAll() {

        return mapper.toParticipantResponseList(participantRepo.findAll());
    }

    public Page<ParticipantResponse> getAllPageable(Pageable pageable) {
        Page<Participant> pagedParticipant = participantRepo.findAll(pageable);
        Page<ParticipantResponse> response = pagedParticipant.map(e -> {
            ParticipantResponse participantResponse = mapper.toParticipantResponse(e);
            return participantResponse;
        });
        return response;
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

    public List<ParticipantResponse> findByEventId(Long id) {
        return mapper.toParticipantResponseList(participantRepo.findByEventId(id));
    }
}
