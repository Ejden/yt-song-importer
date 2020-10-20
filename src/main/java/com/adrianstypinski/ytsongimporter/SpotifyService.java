package com.adrianstypinski.ytsongimporter;

import com.adrianstypinski.ytsongimporter.authorization.spotify.SpotifyToken;
import com.adrianstypinski.ytsongimporter.payload.spotify.PlaylistItem;
import com.adrianstypinski.ytsongimporter.payload.spotify.SpotifyPlaylistItemListResponse;
import com.adrianstypinski.ytsongimporter.payload.spotify.SpotifyUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
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

    public SpotifyUserDetails getUserDetails(String token) {
        String url = "https://api.spotify.com/v1/me";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, getAuthorizationValue(token));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        return restTemplate.exchange(url, HttpMethod.GET, request, SpotifyUserDetails.class).getBody();
    }

    public PlaylistItem createPlaylist(String name, String userId, SpotifyToken token) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://api.spotify.com/v1/users/{user_id}/playlists";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, getAuthorizationValue(token.getAccess_token()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new LinkedHashMap<>();
        body.put("name", name);
        body.put("public", "true");
        body.put("collaborative", "false");
        body.put("description", PLAYLIST_DESCRIPTION);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl(url);

        Map<String, String> pathVars = new LinkedHashMap<>();
        pathVars.put("user_id", userId);

        ResponseEntity<PlaylistItem> newPlaylist = restTemplate.exchange(
                    uriBuilder.buildAndExpand(pathVars).toUri(),
                    HttpMethod.POST,
                    request,
                    PlaylistItem.class);

        log.info("Created new spotify playlist for user {}", userId);
        return newPlaylist.getBody();
    }

    private String getAuthorizationValue(String token) {
        return String.format("Bearer %s", token);
    }
}
