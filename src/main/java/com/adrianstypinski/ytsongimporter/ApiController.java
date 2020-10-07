package com.adrianstypinski.ytsongimporter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api")
public class ApiController {

    private final SpotifyService spotifyService;
    private final YoutubeService youtubeService;

    public ApiController(SpotifyService spotifyService, YoutubeService youtubeService) {
        this.spotifyService = spotifyService;
        this.youtubeService = youtubeService;
    }
}
