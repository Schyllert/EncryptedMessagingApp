package server.application.handlers;

import server.application.Authenticator;
import server.database.DataSource;
import server.models.User;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginHandler {

    private User user;
    private String message;

    public void login(String username, String password) {


        if (username == null || password == null) {
            message = "Need to provide a username and password to login";
            return;
        }

        if(password.length() < 6){
            message = "Password must be at least 6 characters";
            return;
        }

        try {
            Connection conn = DataSource.getInstance().getBds().getConnection();
            user = Authenticator.login(username, password, conn);
            if(user == null){
                message = "Invalid username or password";
                //Logger.log(username, "Unsuccessfully tried to login");
            } else {
                message = "Logged in successfully";
                // Logger.log(username, "Logged in");
            }

        } catch (SQLException e) {
            message = "login failed";
            e.printStackTrace();
        }
    }

    public User getUser(){
        return user;
    }

    public String getMessage(){
        return message;
    }
}
