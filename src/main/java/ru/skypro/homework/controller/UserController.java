package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UserRecord;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "setPassword",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "setPassword",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = NewPassword.class)
                            )
                    )
            },
            tags = "Пользователи"
    )
    @PostMapping("/set_password")
    public NewPassword setPassword(@RequestBody NewPassword newPassword) {
        return userService.setPassword(newPassword);
    }

    @Operation(
            summary = "getUser",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "getUser",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserRecord.class)
                            )
                    )
            },
            tags = "Пользователи"
    )
    @GetMapping("/me")
    public UserRecord getUser() {
        return userService.getUser();
    }

    @Operation(
            summary = "updateUser",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "updateUser",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserRecord.class)
                            )
                    )
            },
            tags = "Пользователи"
    )
    @PatchMapping("/me")
    public UserRecord updateUser(@RequestBody UserRecord userRecord) {
        return userService.updateUser(userRecord);
    }

    @Operation(
            summary = "updateUserImage",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "updateUserImage"
                    )
            },
            tags = "Пользователи"
    )
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "/me/image")
    public void updateUserImage(@RequestPart("image") MultipartFile multipartFile) throws IOException {
        userService.updateUserImage(multipartFile);
    }
}
