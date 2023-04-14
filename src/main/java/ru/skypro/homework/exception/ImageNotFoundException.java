package ru.skypro.homework.exception;

public class ImageNotFoundException extends RuntimeException{
    private final long id;

    public ImageNotFoundException(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
