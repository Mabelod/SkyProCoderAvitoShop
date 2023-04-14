package ru.skypro.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<String> handlesImageNotFoundException(ImageNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(String.format("Image с id = %d не найден", e.getId()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AdsNotFoundException.class)
    public ResponseEntity<String> handlesAdsNotFoundException(AdsNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(String.format("Товар с id = %d не найден", e.getId()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AvatarNotFoundException.class)
    public ResponseEntity<String> handlesAvatarNotFoundException(AvatarNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(String.format("Avatar с id = %d не найден", e.getId()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handlesUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(String.format("Пользователь с id = %d не найден", e.getId()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<String> handlesCommentNotFoundException(CommentNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(String.format("Коментарий с id = %d не найден", e.getId()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(CommentForbiddenException.class)
    public ResponseEntity<String> handlesCommentForbiddenException(CommentForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(String.format("Коментарий с id = %d не найден в текущем товаре", e.getId()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserForbiddenException.class)
    public ResponseEntity<String> handlesUserForbiddenException(UserForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Текущий пароль введен неправильно");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserNameNotFoundException.class)
    public ResponseEntity<String> handleUserNameNotFoundException(UserNameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Пользователь с username " + e.getUsername() + " не найден");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AuthoritiesNotFoundException.class)
    public ResponseEntity<String> handleAuthoritiesNameNotFoundException(AuthoritiesNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Роль с username " + e.getUsername() + " не найдена");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handlesUnauthorizedException(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Нет доступа");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ImageNotFoundFromAdsException.class)
    public ResponseEntity<String> handlesImageNotFoundFromAdsExceptionException(ImageNotFoundFromAdsException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Коментарий не найден");
    }
}
