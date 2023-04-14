package ru.skypro.homework.exception;

public class AuthoritiesNotFoundException extends RuntimeException{
    private final String username;

    public AuthoritiesNotFoundException(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
