package com.adrianstypinski.ytsongimporter;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;

@Service
public class YoutubeService {

    public ResponseEntity<String> getPlaylists(String token) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://www.googleapis.com/youtube/v3/playlists?part=snippet,contentDetails&maxResults=25&mine=true&key=" + System.getenv("YOUTUBE_AUTH_KEY");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.format("Bearer %s", token));
        headers.add("Accept", "application/json");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        return restTemplate.exchange(url, HttpMethod.GET, request, String.class);
    }
}
