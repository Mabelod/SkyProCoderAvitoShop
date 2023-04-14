package ru.skypro.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.DtoMapper;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.*;
import ru.skypro.homework.model.*;
import ru.skypro.homework.repository.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
public class AdsService {
    private final Logger logger = LoggerFactory.getLogger(AdsService.class);
    private final AdsRepository adsRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final DtoMapper dtoMapper;
    private final ImageRepository imageRepository;
    private final AuthoritiesRepository authoritiesRepository;

    public AdsService(AdsRepository adsRepository,
                      CommentRepository commentRepository,
                      UserRepository userRepository,
                      ImageService imageService,
                      DtoMapper dtoMapper,
                      ImageRepository imageRepository,
                      AuthoritiesRepository authoritiesRepository) {
        this.adsRepository = adsRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.imageService = imageService;
        this.dtoMapper = dtoMapper;
        this.imageRepository = imageRepository;
        this.authoritiesRepository = authoritiesRepository;
    }

    /**
     * Метод выводит все объявления
     *
     * @return возвращает объявления
     */
    public ResponseWrapperAds getWrapperAds() {
        logger.info("Was invoked method getWrapperAds");
        return dtoMapper.toResponseWrapperAds(adsRepository.findAll());
    }

    /**
     * Метод для добавления объявление
     *
     * @param createAds тело объявления
     * @param multipartFile картинка
     * @param authentication авторизованный пользователь
     * @return возвращает объявление
     */
    public AdsRecord addAds(CreateAds createAds, MultipartFile multipartFile, Authentication authentication) throws IOException {
        logger.info("Was invoked method addAds");
        User user = userRepository.findByUsername(authentication.getName());
        if (user == null) {
            logger.error("There is not user with username = {}", authentication.getName());
            throw new UserNameNotFoundException(authentication.getName());
        }
        Ads ads = dtoMapper.toCreateAdsEntity(createAds);
        ads.setImage(imageService.uploadImage(multipartFile));
        ads.setUser(user);
        return dtoMapper.toAdsDto(adsRepository.save(ads));
    }

    /**
     * Метод для получения комментариев объявления
     *
     * @param id id объявления
     * @return возвращает список объявлений
     */
    public ResponseWrapperComment getComments(Long id) {
        logger.info("Was invoked method getComments");
        Ads ads = adsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is not ads with id = {}", id);
                    return new AdsNotFoundException(id);
                });
        return dtoMapper.toResponseWrapperComment(ads.getComments());
    }

    /**
     * Метод для добавления комментария к объявлению
     *
     * @param id id объявления
     * @param commentRecord тело комментария
     * @param authentication авторизованный пользователь
     * @return возвращает комментарий
     */
    public CommentRecord addComments(Long id, CommentRecord commentRecord, Authentication authentication) {
        logger.info("Was invoked method addComments");
        LocalDateTime localDateTime = LocalDateTime.now();
        Comment comment = new Comment();
        User user = userRepository.findByUsername(authentication.getName());
        if (user == null) {
            logger.error("There is not user with username = {}", authentication.getName());
            throw new UserNameNotFoundException(authentication.getName());
        }
        Ads ads = adsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is not ads with id = {}", id);
                    return new AdsNotFoundException(id);
                });
        comment.setText(commentRecord.getText());
        comment.setUser(user);
        comment.setAds(ads);
        comment.setCreatedAt(localDateTime);
        return dtoMapper.toCommentDto(commentRepository.save(comment));
    }

    /**
     * Метод для получения информации об объявлении
     *
     * @param id id объявления
     * @return возвращает информацию об объявление и о пользователе, который создал его
     */
    public FullAds getFullAd(Long id) {
        logger.info("Was invoked method getFullAd");
        Ads ads = adsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is not ads with id = {}", id);
                    return new AdsNotFoundException(id);
                });
        return dtoMapper.toFullAds(ads);
    }

    /**
     * Метод для удаления объявления
     *
     * @param id id объявления
     * @param authentication авторизованный пользователь
     */
    public void removeAds(Long id, Authentication authentication) throws IOException {
        logger.info("Was invoked method removeAds");
        Ads ads = adsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is not ads with id = {}", id);
                    return new AdsNotFoundException(id);
                });
        Image oldImage = ads.getImage();
        if (rightsVerification(ads.getUser(), authentication)) {
            commentRepository.deleteAll(ads.getComments());
            adsRepository.deleteById(id);
            if (oldImage == null) {
                logger.error("There is not image");
                throw new ImageNotFoundFromAdsException();
            }
            Path fileToDeletePath = Paths.get(oldImage.getFilePath());
            imageRepository.delete(oldImage);
            Files.delete(fileToDeletePath);
        } else {
            logger.error("Access denied to remove the product");
            throw new UnauthorizedException();
        }
    }

    /**
     * Метод для обновления информации об объявлении
     *
     * @param id id объявления
     * @param createAds тело объявления
     * @param authentication авторизованный пользователь
     * @return возвращает объявление
     */
    public AdsRecord updateAds(Long id, CreateAds createAds, Authentication authentication) {
        logger.info("Was invoked method updateAds");
        Ads ads = adsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is not ads with id = {}", id);
                    return new AdsNotFoundException(id);
                });
        if (rightsVerification(ads.getUser(), authentication)) {
            ads.setDescription(createAds.getDescription());
            ads.setPrice(BigDecimal.valueOf(createAds.getPrice()));
            ads.setTitle(createAds.getTitle());
            return dtoMapper.toAdsDto(adsRepository.save(ads));
        } else {
            logger.error("Access denied to update the product");
            throw new UnauthorizedException();
        }
    }

    /**
     * Метод для получения комментария объявления
     *
     * @param id id объявления
     * @param commentId id коментария
     * @return возвращает коменарий
     */
    public CommentRecord getComment(Long id, Long commentId) {
        logger.info("Was invoked method getComment");
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    logger.error("There is not comment with id = {}", commentId);
                    return new CommentNotFoundException(commentId);
                });
        if (comment.getAds().getId() == id) {
            return dtoMapper.toCommentDto(comment);
        } else {
            logger.error("There is not comment in the product");
            throw new CommentForbiddenException(commentId);
        }
    }

    /**
     * Метод для удаления комментария
     *
     * @param id id объявления
     * @param commentId id коментария
     * @param authentication авторизованный пользователь
     */
    public void deleteComments(Long id, Long commentId, Authentication authentication) {
        logger.info("Was invoked method deleteComments");
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    logger.error("There is not ads with id = {}", commentId);
                    return new CommentNotFoundException(commentId);
                });
        if (rightsVerification(comment.getUser(), authentication)) {
            if (comment.getAds().getId() == id) {
                commentRepository.delete(comment);
            } else {
                logger.error("There is not comment in the product");
                throw new CommentForbiddenException(commentId);
            }
        } else {
            logger.error("Access denied to remove the comment");
            throw new UnauthorizedException();
        }
    }

    /**
     * Метод для  обновления комментария
     *
     * @param id id объявления
     * @param commentId id коментария
     * @param commentRecord тело комментария
     * @param authentication авторизованный пользователь
     * @return возвращает комментарий
     */
    public CommentRecord updateComments(Long id, Long commentId, CommentRecord commentRecord, Authentication authentication) {
        logger.info("Was invoked method updateComments");
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
            logger.error("There is not comment with id = {}", commentId);
            return new CommentNotFoundException(commentId);
        });
        if (rightsVerification(comment.getUser(), authentication)) {
            if (comment.getAds().getId() == id) {
                comment.setText(commentRecord.getText());
                return dtoMapper.toCommentDto(commentRepository.save(comment));
            } else {
                logger.error("There is not comment in the product");
                throw new CommentForbiddenException(commentId);
            }
        } else {
            logger.error("Access denied to update the comment");
            throw new UnauthorizedException();
        }
    }

    /**
     * Метод для обновления картинки объявления
     *
     * @param id id объявления
     * @param multipartFile картинка
     * @param authentication авторизованный пользователь
     */
    public void updateAdsImage(Long id, MultipartFile multipartFile, Authentication authentication) throws IOException {
        logger.info("Was invoked method updateAdsImage");
        Ads ads = adsRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not ads with id = {}", id);
            return new AdsNotFoundException(id);
        });
        Image oldImage = ads.getImage();
        if (rightsVerification(ads.getUser(), authentication)) {
            ads.setImage(imageService.uploadImage(multipartFile));
            adsRepository.save(ads);
            if (oldImage == null) {
                logger.error("There is not image");
                throw new ImageNotFoundFromAdsException();
            }
            Path fileToDeletePath = Paths.get(oldImage.getFilePath());
            imageRepository.delete(oldImage);
            Files.delete(fileToDeletePath);
        } else {
            logger.error("Access denied to update product image ");
            throw new UnauthorizedException();
        }
    }

    /**
     * Метод для получения объявления авторизованного пользователя
     *
     * @param authentication авторизованный пользователь
     * @return возвращает список объявлений
     */
    public ResponseWrapperAds getAdsMe(Authentication authentication) {
        logger.info("Was invoked method getAdsMe");
        User user = userRepository.findByUsername(authentication.getName());
        if (user == null) {
            logger.error("There is not user with username = {}", authentication.getName());
            throw new UserNameNotFoundException(authentication.getName());
        }
        return dtoMapper.toResponseWrapperAds(user.getAds());
    }

    /**
     * Метод для распределения прав авторизованных пользователей и администратора
     *
     * @param user пользователь создавший объявление или комментарий
     * @param authentication авторизованный пользователь
     * @return возвращает true или false
     */
    private boolean rightsVerification(User user, Authentication authentication) {
        Authorities authorities = authoritiesRepository.findByUsername(authentication.getName());
        if (authorities == null) {
            logger.error("There is not role with username = {}", authentication.getName());
            throw new AuthoritiesNotFoundException(authentication.getName());
        }
        return user.getUsername().equals(authentication.getName())
                || authorities.getAuthority().equals("ROLE_ADMIN");
    }
}
