package ru.skypro.homework.exception;

public class CommentForbiddenException extends RuntimeException{
    private final long id;

    public CommentForbiddenException(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
