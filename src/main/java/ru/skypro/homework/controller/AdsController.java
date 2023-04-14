package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("ads")
public class AdsController {
    private final AdsService adsService;
    private final ImageService imageService;

    public AdsController(AdsService adsService,
                         ImageService imageService) {
        this.adsService = adsService;
        this.imageService = imageService;
    }

    @Operation(
            summary = "Получить все объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получить все объявления",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseWrapperAds.class)
                            )
                    )
            },
            tags = "Объявления"
    )
    @GetMapping
    public ResponseWrapperAds getWrapperAds() {
        return adsService.getWrapperAds();
    }

    @Operation(
            summary = "Добавить объявление",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Добавить объявление",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdsRecord.class)
                            )
                    )
            },
            tags = "Объявления"
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AdsRecord addAds(@RequestPart("properties") CreateAds createAds,
                            @RequestPart("image") MultipartFile multipartFile,
                            Authentication authentication) throws IOException {
        return adsService.addAds(createAds, multipartFile, authentication);
    }

    @Operation(
            summary = "Получить комментарии объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получить комментарии объявления",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseWrapperComment.class)
                            )
                    )
            },
            tags = "Комментарии"
    )
    @GetMapping("/{id}/comments")
    public ResponseWrapperComment getComments(@PathVariable Long id) {
        return adsService.getComments(id);
    }

    @Operation(
            summary = "Добавить комментарий к объявлению",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Добавить комментарий к объявлению",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentRecord.class)
                            )
                    )
            },
            tags = "Комментарии"
    )
    @PostMapping("/{id}/comments")
    public CommentRecord addComments(@PathVariable Long id,
                                     @RequestBody CommentRecord commentRecord,
                                     Authentication authentication) {
        return adsService.addComments(id, commentRecord, authentication);
    }

    @Operation(
            summary = "Получить информацию об объявлении",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получить информацию об объявлении",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = FullAds.class)
                            )
                    )
            },
            tags = "Объявления"
    )
    @GetMapping("/{id}")
    public FullAds getFullAd(@PathVariable Long id) {
        return adsService.getFullAd(id);
    }

    @Operation(
            summary = "Удалить объявление",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Удалить объявление"
                    )
            },
            tags = "Объявления"
    )
    @DeleteMapping("/{id}")
    public void removeAds(@PathVariable Long id, Authentication authentication) throws IOException {
        adsService.removeAds(id, authentication);
    }

    @Operation(
            summary = "Обновить информацию об объявлении",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Обновить информацию об объявлении",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdsRecord.class)
                            )
                    )
            },
            tags = "Объявления"
    )
    @PatchMapping("/{id}")
    public AdsRecord updateAds(@PathVariable Long id,
                               @RequestBody CreateAds createAds,
                               Authentication authentication) {
        return adsService.updateAds(id, createAds, authentication);
    }

    @Operation(
            summary = "Получить комментарий объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получить комментарий объявления",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentRecord.class)
                            )
                    )
            },
            tags = "Комментарии"
    )
    @GetMapping("/{adId}/comments/{commentId}")
    public CommentRecord getComment(@PathVariable Long adId,
                                    @PathVariable Long commentId) {
        return adsService.getComment(adId, commentId);
    }

    @Operation(
            summary = "Удалить комментарий",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Удалить комментарий"
                    )
            },
            tags = "Комментарии"
    )
    @DeleteMapping("/{adId}/comments/{commentId}")
    public void deleteComments(@PathVariable Long adId,
                               @PathVariable Long commentId,
                               Authentication authentication) throws RuntimeException {
        adsService.deleteComments(adId, commentId, authentication);
    }

    @Operation(
            summary = "Обновить комментарий",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Обновить комментарий",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentRecord.class)
                            )
                    )
            },
            tags = "Комментарии"
    )
    @PatchMapping("/{adId}/comments/{commentId}")
    public CommentRecord updateComments(@PathVariable Long adId,
                                        @PathVariable Long commentId,
                                        @RequestBody CommentRecord commentRecord,
                                        Authentication authentication) {
        return adsService.updateComments(adId, commentId, commentRecord, authentication);
    }

    @Operation(
            summary = "Получить объявления авторизованного пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получить объявления авторизованного пользователя",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentRecord.class)
                            )
                    )
            },
            tags = "Объявления"
    )
    @GetMapping(path = "/me")
    public ResponseWrapperAds getAdsMe(Authentication authentication) {
        return adsService.getAdsMe(authentication);
    }

    @Operation(
            summary = "Обновить картинку объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Обновить картинку объявления",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentRecord.class)
                            )
                    )
            },
            tags = "Объявления"
    )
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "/{id}/image")
    public void updateAdsImage(@PathVariable Long id,
                               @RequestPart("image") MultipartFile multipartFile,
                               Authentication authentication) throws IOException {
        adsService.updateAdsImage(id, multipartFile, authentication);
    }

    @Operation(
            summary = "Просмотр картинки",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Просмотр картинки"
                    )
            },
            tags = "Объявления"
    )
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> readImage(@Parameter(description = "Введите id image", example = "1")
                                            @PathVariable Long id) {
        Pair<String, byte[]> pair = imageService.readImage(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(pair.getFirst()))
                .contentLength(pair.getSecond().length)
                .body(pair.getSecond());
    }
}
