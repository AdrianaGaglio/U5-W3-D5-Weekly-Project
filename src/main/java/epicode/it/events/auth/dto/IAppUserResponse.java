package epicode.it.events.auth.dto;

import epicode.it.events.auth.appuser.Role;

import java.util.Set;

public interface IAppUserResponse {
    Long getId();
    String getUsername();
    String getEmail();
    Set<Role> getRoles();
}
