package server.models.users;


import server.models.User;

public class UserFactory {
    public static User createUser(String username, String role) {
        switch (role) {
            case ("Member"):
                 return new Member(username);
            case ("Commander"):
                return new Commander(username);
            case ("Admin"):
                return new Admin(username);
            default:
                return null;
        }
    }
}
