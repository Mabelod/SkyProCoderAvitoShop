package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class UserRecord {
    private String email;
    private String firstName;
    private int id;
    private String lastName;
    private String phone;
    private String regDate;
    private String image;
}
