package ru.skypro.homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.DtoMapper;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UserRecord;
import ru.skypro.homework.repository.RepositoryUser;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final RepositoryUser repositoryUser;
    private final DtoMapper dtoMapper;

    public UserService(RepositoryUser repositoryUser, DtoMapper dtoMapper) {
        this.repositoryUser = repositoryUser;
        this.dtoMapper = dtoMapper;
    }

    public NewPassword setPassword(NewPassword newPassword) {
        return null;
    }

    public UserRecord getUser() {
        return null;
    }

    public UserRecord updateUser(UserRecord userRecord) {
        return null;
    }

    public void updateUserImage(MultipartFile multipartFile) {
    }
}
