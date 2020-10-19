package com.adrianstypinski.ytsongimporter;

import com.adrianstypinski.ytsongimporter.payload.YoutubePlaylistItemDto;
import com.adrianstypinski.ytsongimporter.payload.YoutubePlaylistItemListResponse;
import com.adrianstypinski.ytsongimporter.payload.SimplePlaylistItemDtoFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;

import java.util.Objects;
import java.util.Set;
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
}
