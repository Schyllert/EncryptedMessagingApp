package server.models;

public abstract class User {
    private final String username;

    public User(String username) {
        this.username = username;
    }

    public String getUserName(){
        return username;
    }
    public String toString(){
        return username;
    }
}

