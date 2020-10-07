package com.adrianstypinski.ytsongimporter.authorization;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("auth")
public class AuthorizationController {

    private final SpotifyAuthorizationService spotifyAuthService;
    private final YoutubeAuthorizationService youtubeAuthService;

    public AuthorizationController(
            SpotifyAuthorizationService spotifyAuthService,
            YoutubeAuthorizationService youtubeAuthService) {

        this.spotifyAuthService = spotifyAuthService;
        this.youtubeAuthService = youtubeAuthService;
    }

    @GetMapping("spotify")
    public RedirectView getSpotifyAuth(ModelMap model) {
        return spotifyAuthService.getAuthorization(model);
    }

    @GetMapping("youtube")
    public RedirectView getYoutubeAuth(ModelMap model) {
        return youtubeAuthService.getAuthorization(model);
    }
}
