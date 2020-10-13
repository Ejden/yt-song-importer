package com.adrianstypinski.ytsongimporter.model;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDao extends CrudRepository<User, UUID> {

    Optional<User> findBySpotifyAuthCode(String code);

}
