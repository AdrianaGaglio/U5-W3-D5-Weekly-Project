package epicode.it.events.entities.users.participant;

import epicode.it.events.entities.users.EventUser.EventUser;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Data
@Entity
public class Participant extends EventUser {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
