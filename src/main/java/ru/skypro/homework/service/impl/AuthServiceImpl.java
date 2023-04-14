package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.exception.UserNameNotFoundException;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {
    private final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    public AuthServiceImpl(UserDetailsManager manager,
                           UserRepository userRepository) {
        this.manager = manager;
        this.userRepository = userRepository;
        this.encoder = new BCryptPasswordEncoder();
    }

    /**
     * Метод для авторизации пользователя
     *
     * @param username логин пользователя
     * @param password пароль пользователя
     * @return возвращает true или false
     */
    @Override
    public boolean login(String username, String password) {
        logger.info("Was invoke method login");
        if (!manager.userExists(username)) {
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(username);
        String encryptedPassword = userDetails.getPassword();
        String encryptedPasswordWithoutEncryptionType = encryptedPassword.substring(8);
        return encoder.matches(password, encryptedPasswordWithoutEncryptionType);
    }

    /**
     * Метод для регистрации пользователя
     *
     * @param registerReq тело для ригистрации нового пользователя
     * @param role        роль пользователя
     * @return возвращает true или false
     */
    @Override
    public boolean register(RegisterReq registerReq, Role role) {
        logger.info("Was invoke method register");
        if (manager.userExists(registerReq.getUsername())) {
            return false;
        }
        manager.createUser(
//                User.withDefaultPasswordEncoder()
//                        .password(registerReq.getPassword())
//                        .username(registerReq.getUsername())
//                        .roles(role.name())
//                        .build()
                User.withUsername(registerReq.getUsername())
                        .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
                        .password(registerReq.getPassword())
                        .roles(role.name())
                        .build()
        );
        ru.skypro.homework.model.User user = userRepository.findByUsername(registerReq.getUsername());
        if (user == null) {
            logger.error("There is not user with name = {}", registerReq.getUsername());
            throw new UserNameNotFoundException(registerReq.getUsername());
        }
        user.setFirstName(registerReq.getFirstName());
        user.setLastName(registerReq.getLastName());
        user.setPhone(registerReq.getPhone());
        user.setRegDate(LocalDateTime.now());
        user.setEmail(registerReq.getUsername());
        userRepository.save(user);
        return true;
    }
}
