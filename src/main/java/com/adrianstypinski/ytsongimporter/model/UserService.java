package com.adrianstypinski.ytsongimporter.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService {
    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User saveUser(User user) {
        log.info("Saved user {}", user);
        return userDao.save(user);
    }

    public Optional<User> findUserById(UUID uuid) {
        return userDao.findById(uuid);
    }

    public Optional<User> findBySpotifyAuthCode(String authCode) {
        return userDao.findBySpotifyAuthCode(authCode);
    }
}
