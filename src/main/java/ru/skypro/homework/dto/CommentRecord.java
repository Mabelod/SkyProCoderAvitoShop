package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class CommentRecord {
    private int author;
    private String createdAt;
    private int pk;
    private String text;
}
