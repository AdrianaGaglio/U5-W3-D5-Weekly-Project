package epicode.it.events.utils;

import epicode.it.events.entities.event.Event;
import epicode.it.events.entities.users.EventUser.EventUser;

public class Utils {

    public static String getAvatar(EventUser u) {
        return "https://ui-avatars.com/api/?name=" + u.getName() + "+" + u.getSurname();
    }

}
