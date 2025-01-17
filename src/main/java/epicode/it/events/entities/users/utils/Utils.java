package epicode.it.events.entities.users.utils;

import epicode.it.events.entities.users.EventUser.EventUser;

public class Utils {

    public static String getAvatar(EventUser u) {
        return "https://ui-avatars.com/api/?name=" + u.getName() + "+" + u.getSurname();
    }

}
