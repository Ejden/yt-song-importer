package com.adrianstypinski.ytsongimporter.payload.spotify;

import lombok.Data;

@Data
public class SpotifyTrack {
    public static final String QUERY_TYPE = "track";

    private String id;
    private String name;
    private String uri;
}
