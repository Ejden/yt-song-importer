package com.adrianstypinski.ytsongimporter;

import com.adrianstypinski.ytsongimporter.payload.SpotifyPlaylistItemListResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Set;

@Service
public class SpotifyService {

    public Set<SpotifyPlaylistItemListResponse.PlaylistItem> getPlaylists(String token) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://api.spotify.com/v1/me/playlists?limit=50";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.format("Bearer %s", token));
        headers.add("Accept", "application/json");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<SpotifyPlaylistItemListResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, SpotifyPlaylistItemListResponse.class);

        Set<SpotifyPlaylistItemListResponse.PlaylistItem> playlists = Set.copyOf(Objects.requireNonNull(response.getBody()).getItems());

        return playlists;
    }
}
