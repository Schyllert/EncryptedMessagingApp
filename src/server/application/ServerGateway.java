package server.application;

import org.apache.commons.lang3.StringUtils;
import server.Server;
import server.application.handlers.*;
import server.exceptions.DisplayToUserException;
import server.models.User;

import java.util.Arrays;


public class ServerGateway {

    User user;
    private final Server server;

    String helpString = "\nAvailable commands:\n" +
            "login <username> <password>\n" +
            "logout\n";

    public ServerGateway(Server server) {
        this.server = server;
    }

    public String handle(String request, String data) throws Exception {

        String command = request.split(" ")[0];
        System.out.println("This is our command: " + command);

        String[] args = getArgs(request); //TODO: Check nbr of args for each command
        System.out.println("args " + Arrays.toString(args));
        if (command.equals("help")) return helpString;

        if (user == null) {
            String username = null;
            String password = null;

            if (args.length > 1) {
                username = args[0];
                password = args[1];
            }

            if (command.equals("login")) {
                LoginHandler loginHandler = new LoginHandler();
                loginHandler.login(username, password);
                user = loginHandler.getUser();
                return loginHandler.getMessage();
            } else if (command.equals("create-user")) {
                return "";
            } else {
                //TODO
                throw new DisplayToUserException("Login required");
            }
        }

        return switch (command) {
            case "login" -> "You are already logged in as: " + user.getUserName();
            case "logout" -> {
                user = null;
                yield ("You have successfully logged out");
            }
            case "pm" -> {
                String receiver = request.split(" ")[1];
                yield serverMessageHandler.handle(user, server, receiver, data);
            }

            case "create-user" -> {
                String message;
                if (AccessControlManager.canCreate(user)) {
                    message = CreateUserHandler.handle(user, args);
                } else {
                    message = "Not enough privilege to create a new user";
                }
                yield message;
            }
            default -> "Invalid command. Write \"help\" for list of commands.";
        };
    }

    private String[] getArgs(String request) {
        String[] requestComponents = request.split(" ");
        if (requestComponents.length == 1) return new String[0];
        return Arrays.copyOfRange(requestComponents, 1, requestComponents.length);
    }

    public User getUser() {
        return user;
    }


}
