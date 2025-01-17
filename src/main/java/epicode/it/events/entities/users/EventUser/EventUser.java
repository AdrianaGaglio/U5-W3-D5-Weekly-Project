package epicode.it.events.entities.users.EventUser;

import epicode.it.events.auth.appuser.AppUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="event_users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
public class EventUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private String surname;

    private String image;

    @OneToOne
    @JoinColumn(name = "user_id",  unique = true)
    private AppUser appUser;

}