package epicode.it.events.entities.users.EventUser;

import epicode.it.events.auth.appuser.AppUserRepo;
import epicode.it.events.auth.appuser.AppUserSvc;
import epicode.it.events.entities.users.EventUser.dto.EventUserCreateRequest;
import epicode.it.events.entities.users.EventUser.dto.EventUserUpdateRequest;
import epicode.it.events.entities.users.participant.Participant;
import epicode.it.events.entities.users.planner.Planner;
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
public class EventUserSvc {
    private final EventUserRepo eventUserRepo;
    private final AppUserRepo appUserRepo;

    public List<EventUser> getAll() {
        return eventUserRepo.findAll();
    }

    public Page<EventUser> getAllPageable(Pageable pageable) {

        return eventUserRepo.findAll(pageable);
    }

    public EventUser getById(Long id) {
        return eventUserRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public int count() {

        return (int) eventUserRepo.count();
    }

    public String delete(Long id) {
        EventUser e = getById(id);
        eventUserRepo.delete(e);
        return "User deleted successfully";
    }

    public String delete(EventUser e) {
        EventUser foundEventUser = getById(e.getId());
        eventUserRepo.delete(foundEventUser);
        return "User deleted successfully";
    }

    public EventUser getByNameAndSurname(String name, String surname) {
        return eventUserRepo.findByNameAndSurname(name, surname);
    }

    public EventUser create(@Valid EventUserCreateRequest request, boolean isPlanner) {
        if (eventUserRepo.existsByNameAndSurname(request.getName(), request.getSurname()))
            throw new EntityExistsException("Planner already exists");

        EventUser u = isPlanner ? new Planner() : new Participant();
        BeanUtils.copyProperties(request, u);
        boolean hasImage = request.getImage() != null && !request.getImage().isEmpty();
        u.setImage(hasImage ? request.getImage() : Utils.getAvatar(u));
        u.setAppUser(appUserRepo.findById(request.getUserId()).orElse(null));
        return eventUserRepo.save(u);
    }

    public EventUser update(Long id, @Valid EventUserUpdateRequest request) {
        EventUser u = getById(id);
        BeanUtils.copyProperties(request, u);
        return eventUserRepo.save(u);
    }
}
