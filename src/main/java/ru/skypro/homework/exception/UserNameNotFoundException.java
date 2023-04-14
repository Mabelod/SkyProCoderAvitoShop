package ru.skypro.homework.exception;

public class UserNameNotFoundException extends RuntimeException{
    private final String username;

    public UserNameNotFoundException(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
