package com.adrianstypinski.ytsongimporter.model;

import com.adrianstypinski.ytsongimporter.payload.SpotifyResponse;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "temp_users")
public class User {
    @Transient
    public static final String ATTRIBUTE_NAME = "USER";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(length = 1024)
    private String spotifyAuthCode;
    @Column(length = 1024)
    private String spotifyAuthToken;

    @Transient
    private SpotifyResponse spotifyToken;
}
