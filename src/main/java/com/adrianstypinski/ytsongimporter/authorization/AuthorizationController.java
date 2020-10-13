package com.adrianstypinski.ytsongimporter.authorization;

import com.adrianstypinski.ytsongimporter.exepctions.InvalidCodeException;
import com.adrianstypinski.ytsongimporter.exepctions.NoAttributesProvidedException;
import com.adrianstypinski.ytsongimporter.exepctions.UserNotFoundException;
import com.adrianstypinski.ytsongimporter.payload.SpotifyResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

    /**
     * @return RedirectView to Spotify Authorization site
     */
    @GetMapping("spotify/requestCode")
    public RedirectView getSpotifyAuth() throws NoAttributesProvidedException {
        return spotifyAuthService.getAuthorizationCode();
    }

    /**
     *
     * @param response where we're adding user cookie
     * @param code from spotify API
     * @param session which stores user SessionID
     * @return dashboard view
     * @throws UserNotFoundException when provided code from spotify is wrong or not provided
     */
    @GetMapping("spotify/acceptAuthCode")
    public String acceptCode(HttpServletResponse response, @RequestParam String code, HttpSession session) throws UserNotFoundException {
        SpotifyResponse spotifyResponse = spotifyAuthService.getAuthorizationTokenBySecret(code, session);

        Cookie accessToken = new Cookie("spotifyAccessToken", spotifyResponse.getAccess_token());
        accessToken.setHttpOnly(true);
        accessToken.setMaxAge(spotifyResponse.getExpires_in());

        response.addCookie(accessToken);

        return "dashboard";
    }

    @GetMapping("youtube")
    public RedirectView getYoutubeAuth() {
        return youtubeAuthService.getAuthorization();
    }
}
