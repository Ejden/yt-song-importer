package com.adrianstypinski.ytsongimporter;

import com.adrianstypinski.ytsongimporter.authorization.spotify.SpotifyToken;
import com.adrianstypinski.ytsongimporter.payload.PlaylistItem;
import com.adrianstypinski.ytsongimporter.payload.SpotifyPlaylistItemListResponse;
import com.sun.istack.NotNull;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;
import java.util.Set;

@Service
public class SpotifyService {
    private final String CONTENT_TYPE = "application/json";
    private final String PLAYLIST_DESCRIPTION = "This playlist is generated via Yt Song Importer, written by Adrian Stypiński. Github: @Ejden";

    public Set<PlaylistItem> getPlaylists(String token) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://api.spotify.com/v1/me/playlists?limit=50";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.format("Bearer %s", token));
        headers.add("Accept", "application/json");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<SpotifyPlaylistItemListResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, SpotifyPlaylistItemListResponse.class);

        return Set.copyOf(Objects.requireNonNull(response.getBody()).getItems());
    }

    public String createPlaylist(String name, String userId, SpotifyToken token) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://api.spotify.com/v1/users/11181989165/playlists";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, getAuthorizationValue(token.getAccess_token()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("name", name);
        body.add("public", "true");
        body.add("collaborative", "false");
        body.add("description", PLAYLIST_DESCRIPTION);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl(url)
                .queryParam("user_id", userId);

        ResponseEntity<String> newPlaylist = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                String.class);

        return newPlaylist.getBody();
    }

    private String getAuthorizationValue(String token) {
        return String.format("Bearer %s", token);
    }
}
