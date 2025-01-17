package epicode.it.events.auth.appuser;

import epicode.it.events.auth.dto.IAppUserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface AppUserRepo extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByEmail(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM AppUser u WHERE LOWER(u.username) = LOWER(:usernameOrEmail) OR LOWER(u.email) = LOWER(:usernameOrEmail)")
    Optional<AppUser> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);

    @Query("SELECT u FROM AppUser u WHERE u.username = :username")
    IAppUserResponse findIAppUserResponseByUsername(@Param("username") String username);
}
