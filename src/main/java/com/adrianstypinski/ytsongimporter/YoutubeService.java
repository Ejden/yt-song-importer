package com.adrianstypinski.ytsongimporter;

import com.adrianstypinski.ytsongimporter.payload.youtube.YoutubePlaylistItemDto;
import com.adrianstypinski.ytsongimporter.payload.youtube.YoutubePlaylistItemListResponse;
import com.adrianstypinski.ytsongimporter.payload.SimplePlaylistItemDtoFactory;
import com.adrianstypinski.ytsongimporter.payload.youtube.YoutubePlaylistItems;
import com.adrianstypinski.ytsongimporter.payload.youtube.YoutubeVideo;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class YoutubeService {

    public Set<YoutubePlaylistItemDto> getPlaylists(String token) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://www.googleapis.com/youtube/v3/playlists?part=snippet,contentDetails&maxResults=25&mine=true&key=" + System.getenv("YOUTUBE_AUTH_KEY");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.format("Bearer %s", token));
        headers.add("Accept", "application/json");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<YoutubePlaylistItemListResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, YoutubePlaylistItemListResponse.class);
        log.info("Downloaded playlists from Google API");

        return Objects.requireNonNull(response.getBody())
                    .getItems()
                    .stream()
                    .map(SimplePlaylistItemDtoFactory::toPlaylistItemDto)
                    .collect(Collectors.toSet());
    }

    public Set<YoutubeVideo> getVideos(String playlistId, GoogleTokenResponse googleToken) {
        Set<YoutubeVideo> youtubeVideos = new HashSet<>();
        String pageToken = null;

        do {
            // Getting videos from Youtube API
            YoutubePlaylistItems youtubePlaylistItems = getVideosSinglePage(pageToken, playlistId, googleToken);
            // Adding downloaded token to set
            youtubeVideos.addAll(youtubePlaylistItems.getItems());
            // Getting token for next page of videos
            pageToken = youtubePlaylistItems.getNextPageToken();
        } while (pageToken != null);

        return youtubeVideos;
    }

    private YoutubePlaylistItems getVideosSinglePage(String pageToken, String playlistId, GoogleTokenResponse googleToken) {
        String url = "https://www.googleapis.com/youtube/v3/playlistItems";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.AUTHORIZATION, googleToken.getAccessToken());

        HttpEntity<Map<String, String>> request = new HttpEntity<>(headers);

        UriComponentsBuilder uriBuilder = createUriForVideosQuery(url, pageToken, playlistId);

        return restTemplate.exchange(
            uriBuilder.toUriString(),
            HttpMethod.GET,
            request,
            YoutubePlaylistItems.class
        ).getBody();
    }

    private UriComponentsBuilder createUriForVideosQuery(String url, String pageToken, String playlistId) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(url)
                .queryParam("part", "snippet")
                .queryParam("playlistId", playlistId)
                .queryParam("maxResults", 50)
                .queryParam("key", System.getenv("YOUTUBE_AUTH_KEY"));

        return (pageToken == null) ? builder : builder.queryParam("pageToken", pageToken);
    }
}
