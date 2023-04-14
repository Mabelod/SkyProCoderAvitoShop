package ru.skypro.homework.exception;

public class AdsNotFoundException extends RuntimeException{
    private final long id;

    public AdsNotFoundException(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
