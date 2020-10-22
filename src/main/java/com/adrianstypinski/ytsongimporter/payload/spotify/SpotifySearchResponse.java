package com.adrianstypinski.ytsongimporter.payload.spotify;

import lombok.Data;

import java.util.List;

@Data
public class SpotifySearchResponse {
    private SpotifyTracks tracks;

    @Data
    public static class SpotifyTracks {
        private List<SpotifyTrack> items;
    }
}
