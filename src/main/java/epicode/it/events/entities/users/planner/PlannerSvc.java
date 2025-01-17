package epicode.it.events.entities.users.planner;

import epicode.it.events.entities.users.planner.dto.PlannerResponse;
import epicode.it.events.entities.users.planner.dto.PlannerResponseMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlannerSvc {
    private final PlannerRepo plannerRepo;
    private final PlannerResponseMapper mapper;

    public List<PlannerResponse> getAll() {

        return mapper.toPlannerResponseList(plannerRepo.findAll());
    }

    public Page<PlannerResponse> getAllPageable(Pageable pageable) {
        Page<Planner> pagedParticipant = plannerRepo.findAll(pageable);
        Page<PlannerResponse> response = pagedParticipant.map(e -> {
            PlannerResponse participantResponse = mapper.toPlannerResponse(e);
            return participantResponse;
        });
        return response;
    }

    public Planner getById(Long id) {
        return plannerRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Planner not found"));
    }

    public int count() {
        return (int) plannerRepo.count();
    }

    public String delete(Long id) {
        Planner e = getById(id);
        plannerRepo.delete(e);
        return "Planner deleted successfully";
    }

    public String delete(Planner e) {
        Planner foundPlanner = getById(e.getId());
        plannerRepo.delete(foundPlanner);
        return "Planner deleted successfully";
    }


}
