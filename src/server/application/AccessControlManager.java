package server.application;
import server.models.User;

public class AccessControlManager {


    public static boolean canCreate(User user) {
        switch (user.getClass().getSimpleName()) {
            case ("Admin"):
                return true;
            default:
                return false;
        }
    }

    public static boolean canKick(User user) {
        switch (user.getClass().getSimpleName()) {
            case("Admin"):
                case("Commander"):
                    return true;
                    default:
                        return false;
        }
    }
}



