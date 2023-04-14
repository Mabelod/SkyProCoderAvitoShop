package ru.skypro.homework.exception;

public class CommentNotFoundException extends RuntimeException{
    private final long id;

    public CommentNotFoundException(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
