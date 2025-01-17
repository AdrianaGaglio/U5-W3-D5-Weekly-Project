package epicode.it.events.entities.users.planner;

import epicode.it.events.entities.users.EventUser.dto.EventUserCreateRequest;
import epicode.it.events.entities.users.EventUser.dto.EventUserUpdateRequest;
import epicode.it.events.entities.users.participant.Participant;
import epicode.it.events.entities.users.utils.Utils;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlannerSvc {
    private final PlannerRepo plannerRepo;

    public List<Planner> getAll() {

        return plannerRepo.findAll();
    }

    public Page<Planner> getAllPageable(Pageable pageable) {
        return plannerRepo.findAll(pageable);
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
