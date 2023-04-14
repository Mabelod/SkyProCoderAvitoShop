package ru.skypro.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.DtoMapper;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UserRecord;
import ru.skypro.homework.exception.UserNameNotFoundException;
import ru.skypro.homework.model.Avatar;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AvatarRepository;
import ru.skypro.homework.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;
    private final AvatarService avatarService;
    private final AvatarRepository avatarRepository;
    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository,
                       DtoMapper dtoMapper,
                       AvatarService avatarService,
                       AvatarRepository avatarRepository,
                       UserDetailsManager manager) {
        this.userRepository = userRepository;
        this.dtoMapper = dtoMapper;
        this.avatarService = avatarService;
        this.avatarRepository = avatarRepository;
        this.manager = manager;
        this.encoder = new BCryptPasswordEncoder();
    }

    /**
     * Метод для обновления пароля
     *
     * @param newPassword тело с текущим паролем и новым паролем
     * @return возвращает тело с текущим паролем и новым паролем
     */
    public NewPassword setPassword(NewPassword newPassword) {
        logger.info("Was invoked method setPassword");
        String encryptedPassword = "{bcrypt}" + encoder.encode(newPassword.getNewPassword());
        manager.changePassword(newPassword.getCurrentPassword(), encryptedPassword);
        return newPassword;
    }

    /**
     * Метод для получения информации об авторизованном пользователе
     *
     * @param authentication авторизованный пользователь
     * @return возвращает пользователя
     */
    public UserRecord getUser(Authentication authentication) {
        logger.info("Was invoked method getUser");
        User user = userRepository.findByUsername(authentication.getName());
        if (user == null) {
            logger.error("There is not user with username = {}", authentication.getName());
            throw new UserNameNotFoundException(authentication.getName());
        }
        return dtoMapper.toUserDto(user);
    }

    /**
     *  Метод обновления информации об авторизованном пользователе
     *
     * @param userRecord пользователь
     * @param authentication авторизованный пользователь
     * @return возвращает пользователя
     */
    public UserRecord updateUser(UserRecord userRecord, Authentication authentication) {
        logger.info("Was invoked method getUser");
        User user = userRepository.findByUsername(authentication.getName());
        if (user == null) {
            logger.error("There is not user with username = {}", authentication.getName());
            throw new UserNameNotFoundException(authentication.getName());
        }
        user.setFirstName(userRecord.getFirstName());
        user.setLastName(userRecord.getLastName());
        user.setPhone(userRecord.getPhone());
        return dtoMapper.toUserDto(userRepository.save(user));
    }

    /**
     * Метод для обновления аватара авторизованного пользователя
     *
     * @param multipartFile аватар
     * @param authentication авторизованный пользователь
     */
    public void updateUserImage(MultipartFile multipartFile, Authentication authentication) throws IOException {
        logger.info("Was invoked method updateUserImage");
        User user = userRepository.findByUsername(authentication.getName());
        if (user == null) {
            logger.error("There is not user with username = {}", authentication.getName());
            throw new UserNameNotFoundException(authentication.getName());
        }
        Avatar oldAvatar = user.getAvatar();
        user.setAvatar(avatarService.uploadAvatar(multipartFile));
        userRepository.save(user);
        if (oldAvatar != null) {
            Path fileToDeletePath = Paths.get(oldAvatar.getFilePath());
            avatarRepository.delete(oldAvatar);
            Files.delete(fileToDeletePath);
        }
    }
}
