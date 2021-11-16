package server.application.handlers;

import server.Server;
import server.models.User;

public class CreateUserHandler {
    public static String handle(User user, String[] args) throws Exception {
        if (args == null || args.length < 2) {
            return "Invalid arguments";
        }

        return "";
    }
}
