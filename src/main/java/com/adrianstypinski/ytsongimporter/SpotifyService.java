package com.adrianstypinski.ytsongimporter;

import com.adrianstypinski.ytsongimporter.authorization.spotify.SpotifyToken;
import com.adrianstypinski.ytsongimporter.payload.spotify.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
public class SpotifyService {
    private final String CONTENT_TYPE = "application/json";
    private final String PLAYLIST_DESCRIPTION = "This playlist is generated via Yt Song Importer, written by Adrian Stypiński. Github: @Ejden";

    public Set<SpotifyPlaylist> getPlaylists(SpotifyToken token) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://api.spotify.com/v1/me/playlists?limit=50";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, getAuthorizationValue(token));
        headers.add("Accept", "application/json");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<SpotifyPlaylistItemListResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, SpotifyPlaylistItemListResponse.class);

        return Set.copyOf(Objects.requireNonNull(response.getBody()).getItems());
    }

    public SpotifyUserDetails getUserDetails(SpotifyToken token) {
        String url = "https://api.spotify.com/v1/me";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, getAuthorizationValue(token));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        return restTemplate.exchange(url, HttpMethod.GET, request, SpotifyUserDetails.class).getBody();
    }

    public SpotifyPlaylist createPlaylist(String name, String userId, SpotifyToken token) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://api.spotify.com/v1/users/{user_id}/playlists";

        HttpHeaders headers = createCommonHeadersWithAuthorization(token);

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

        ResponseEntity<SpotifyPlaylist> newPlaylist = restTemplate.exchange(
                    uriBuilder.buildAndExpand(pathVars).toUri(),
                    HttpMethod.POST,
                    request,
                    SpotifyPlaylist.class);

        log.info("Created new spotify playlist for user {}", userId);
        return newPlaylist.getBody();
    }

    public SpotifyTrack searchForTrack(SpotifyToken token, String query) {
        log.info("Searching in Spotify API for track with query {}", query);
        if (query == null) return null;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, getAuthorizationValue(token));

        HttpEntity<Map<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<SpotifySearchResponse> response = restTemplate.exchange(
                getUriForSearchingTracks(query).toUriString(),
                HttpMethod.GET,
                request,
                SpotifySearchResponse.class
        );

        try {
            return Objects.requireNonNull(response.getBody()).getTracks().getItems().get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public void addTrackToPlaylist(SpotifyToken token, String playlistId, String trackUri) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = createCommonHeadersWithAuthorization(token);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                getUriForAddingTrackToPlaylist(playlistId, trackUri),
                HttpMethod.POST,
                request,
                String.class
        );

        log.info("Spotify Api responded with code name: {} while adding new song with URI: {} to playlist with playlistId: {}", response.getStatusCode().getReasonPhrase(), trackUri, playlistId);
    }

    public SpotifyPlaylist getPlaylist(SpotifyToken token, String playlistId) {
        String url = "https://api.spotify.com/v1/playlists/{playlist_id}";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = createCommonHeadersWithAuthorization(token);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(headers);

        Map<String, String> pathVars = new LinkedHashMap<>();
        pathVars.put("playlist_id", playlistId);

        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .buildAndExpand(pathVars)
                .toUri();

        return restTemplate.exchange(
            uri,
            HttpMethod.GET,
            request,
            SpotifyPlaylist.class
        ).getBody();
    }

    private URI getUriForAddingTrackToPlaylist(String playlistId, String trackUri) {
        String url = "https://api.spotify.com/v1/playlists/{playlist_id}/tracks";

        // Defining path variables
        Map<String, String> pathVars = new LinkedHashMap<>();
        pathVars.put("playlist_id", playlistId);

        return UriComponentsBuilder
                .fromHttpUrl(url)
                .queryParam("uris", trackUri)
                .buildAndExpand(pathVars)
                .toUri();
    }

    private UriComponentsBuilder getUriForSearchingTracks(String query) {
        String url = "https://api.spotify.com/v1/search";

        return UriComponentsBuilder
                .fromHttpUrl(url)
                .queryParam("q", query)
                .queryParam("type", SpotifyTrack.QUERY_TYPE)
                .queryParam("limit", 1)
                .queryParam("offset", 0);
    }

    private String getAuthorizationValue(SpotifyToken token) {
        return String.format("Bearer %s", token.getAccess_token());
    }

    private HttpHeaders createCommonHeadersWithAuthorization(SpotifyToken token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, getAuthorizationValue(token));
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }
}
