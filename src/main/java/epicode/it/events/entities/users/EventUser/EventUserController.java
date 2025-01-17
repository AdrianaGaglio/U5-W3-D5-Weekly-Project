package epicode.it.events.entities.users.EventUser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import epicode.it.events.entities.users.EventUser.dto.EventUserCreateRequest;
import epicode.it.events.entities.users.EventUser.dto.EventUserResponse;
import epicode.it.events.entities.users.EventUser.dto.EventUserUpdateRequest;
import epicode.it.events.entities.users.participant.Participant;
import epicode.it.events.utils.upload.UploadSvc;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/event-users")
@RequiredArgsConstructor
public class EventUserController {
    private final EventUserSvc eventUserSvc;
    private final UploadSvc uploadSvc;

//    @PostMapping(consumes = {"multipart/form-data"})
//    public ResponseEntity<EventUserResponse> create(@RequestParam("userRequest") String requestJson,
//                                                    @RequestParam(value = "image", required = false) MultipartFile image,
//                                                    @RequestParam boolean isPlanner) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        EventUserCreateRequest request;
//
//        try {
//            request = objectMapper.readValue(requestJson, EventUserCreateRequest.class);
//            if (image != null) {
//                request.setImage(uploadSvc.uploadFile(image));
//                request.setUserId(null);
//            }
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        return new ResponseEntity<>(eventUserSvc.create(request, isPlanner), HttpStatus.CREATED);
//    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || isAuthenticated()")
    public ResponseEntity<EventUserResponse> update(@PathVariable Long id, @RequestBody EventUserUpdateRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        StringBuilder roles = new StringBuilder();
        authentication.getAuthorities().forEach(authority -> roles.append(authority.getAuthority()).append(""));
        if (!roles.toString().contains("ROLE_ADMIN")) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            EventUser user = eventUserSvc.getById(id);
            if (!user.getAppUser().getUsername().equals(username)) {
                throw new AccessDeniedException("You cannot access this event");
            }
        }

        return new ResponseEntity<>(eventUserSvc.update(id, request), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || isAuthenticated()")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        StringBuilder roles = new StringBuilder();
        authentication.getAuthorities().forEach(authority -> roles.append(authority.getAuthority()).append(""));
        if (!roles.toString().contains("ROLE_ADMIN")) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            EventUser user = eventUserSvc.getById(id);
            if (!user.getAppUser().getUsername().equals(username)) {
                throw new AccessDeniedException("You cannot access this event");
            }
        }

        return new ResponseEntity<>(eventUserSvc.delete(id), HttpStatus.NO_CONTENT);
    }


}
