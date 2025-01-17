package epicode.it.events.entities.users.planner.dto;

import epicode.it.events.entities.users.planner.Planner;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlannerResponseMapper {
    private ModelMapper modelMapper = new ModelMapper();


    public PlannerResponse toPlannerResponse(Planner u) {
        PlannerResponse response = modelMapper.map(u, PlannerResponse.class);
        response.setUsername(u.getAppUser().getUsername());
        response.setEmail(u.getAppUser().getEmail());
        return response;
    }

    public List<PlannerResponse> toPlannerResponseList(List<Planner> users) {
        return users.stream().map(this::toPlannerResponse).toList();
    }
}
