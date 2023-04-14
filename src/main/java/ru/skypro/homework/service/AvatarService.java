package ru.skypro.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.AvatarNotFoundException;
import ru.skypro.homework.model.Avatar;
import ru.skypro.homework.repository.AvatarRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;


@Service
public class AvatarService {
    private final Logger logger = LoggerFactory.getLogger(AvatarService.class);
    @Value("${path.to.avatars.folder}")
    private String avatarsDir;
    private final AvatarRepository avatarRepository;

    public AvatarService(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

    /**
     * Метод для загрузки аватара
     *
     * @param multipartFile аватар
     * @return возвращает аватар
     * @throws IOException
     */
    public Avatar uploadAvatar(MultipartFile multipartFile) throws IOException {
        logger.info("Was invoked method for upload avatar");
        byte[] data = multipartFile.getBytes();

        Avatar avatar = creat(multipartFile.getSize(), multipartFile.getContentType(), data);

        String extension = Optional.ofNullable(multipartFile.getOriginalFilename())
                .map(s -> s.substring(multipartFile.getOriginalFilename().lastIndexOf(".")))
                .orElse("");
        Path path = Paths.get(avatarsDir).resolve(avatar.getId() + extension);
        Files.createDirectories(path.getParent());
        Files.deleteIfExists(path);
        Files.write(path, data);
        avatar.setFilePath(path.toString());
        return avatarRepository.save(avatar);
    }

    /**
     * Метод для создания аватара
     *
     * @param size размер аватара
     * @param contentType тип аватара
     * @param data информация о аватаре
     * @return возвращает аватар
     */
    private Avatar creat(long size, String contentType, byte[] data) {
        logger.info("Was invoked method for create avatar");
        Avatar avatar = new Avatar();
        avatar.setData(data);
        avatar.setFileSize(size);
        avatar.setMediaType(contentType);
        return avatarRepository.save(avatar);
    }

    /**
     * Метод для просмотра аватара из БД
     *
     * @param id id фотографии
     * @return возвращает pair
     */
    public Pair<String, byte[]> readAvatar(long id) {
        logger.info("Was invoked method for read avatar");
        Avatar avatar = avatarRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is not avatar with id = {}", id);
                    return new AvatarNotFoundException(id);
                });
        return Pair.of(avatar.getMediaType(), avatar.getData());
    }
}
