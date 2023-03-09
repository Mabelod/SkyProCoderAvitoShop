package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.DtoMapper;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.repository.RepositoryAds;
import ru.skypro.homework.repository.RepositoryComment;

import java.io.IOException;

@Service
public class AdsService {
    private final RepositoryAds repositoryAds;
    private final RepositoryComment repositoryComment;
    private final DtoMapper dtoMapper;

    public AdsService(RepositoryAds repositoryAds,
                      RepositoryComment repositoryComment,
                      DtoMapper dtoMapper) {
        this.repositoryAds = repositoryAds;
        this.repositoryComment = repositoryComment;
        this.dtoMapper = dtoMapper;
    }

    public ResponseWrapperAds getWrapperAds() {
        return null;
    }

    public AdsRecord addAds(CreateAds createAds, MultipartFile multipartFile) throws IOException {
        return null;
    }

    public ResponseWrapperComment getComments(Long id) {
        return null;
    }

    public CommentRecord addComments(Long id, CommentRecord commentRecord) {
        return null;
    }

    public FullAds getFullAd(Long id) {
        return null;
    }

    public void removeAds(Long id) {
    }

    public AdsRecord updateAds(Long id, CreateAds createAds) {
        return null;
    }

    public CommentRecord getCommentsAd(Long id, Long commentId) {
        return null;
    }

    public void deleteComments(Long id, Long commentId) {
    }

    public CommentRecord updateComments(Long id, Long commentId, CommentRecord commentRecord) {
        return null;
    }

    public String updateAdsImage(Long id, MultipartFile multipartFile) {
        return null;
    }
}
