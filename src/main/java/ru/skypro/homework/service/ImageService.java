package ru.skypro.homework.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.ImageNotFoundException;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.repository.ImageRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;


@Service
public class ImageService {
    private final Logger logger = LoggerFactory.getLogger(ImageService.class);
    @Value("${path.to.images.folder}")
    private String imagesDir;
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    /**
     * Метод для загрузки картинки
     *
     * @param multipartFile картинка
     * @return возвращает картинку
     * @throws IOException
     */
    public Image uploadImage(MultipartFile multipartFile) throws IOException {
        logger.info("Was invoked method for upload image");
        byte[] data = multipartFile.getBytes();

        Image image = creat(multipartFile.getSize(), multipartFile.getContentType(), data);

        String extension = Optional.ofNullable(multipartFile.getOriginalFilename())
                .map(s -> s.substring(multipartFile.getOriginalFilename().lastIndexOf(".")))
                .orElse("");
        Path path = Paths.get(imagesDir).resolve(image.getId() + extension);
        Files.createDirectories(path.getParent());
        Files.deleteIfExists(path);
        Files.write(path, data);
        image.setFilePath(path.toString());
        return imageRepository.save(image);
    }

    /**
     * Метод для создания картинки
     *
     * @param size размер картинки
     * @param contentType тип картинки
     * @param data информация о картинке
     * @return возвращает картинку
     */
    private Image creat(long size, String contentType, byte[] data) {
        Image image = new Image();
        image.setFileSize(size);
        image.setMediaType(contentType);
        image.setData(data);
        return imageRepository.save(image);
    }

    /**
     * Метод для просмотра картинки из БД
     *
     * @param id id картинки
     * @return возвращает картинку
     */
    public Pair<String, byte[]> readImage(long id) {
        logger.info("Was invoked method for read image");
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is not image with id = {}", id);
                    return new ImageNotFoundException(id);
                });
        return Pair.of(image.getMediaType(), image.getData());
    }

}
