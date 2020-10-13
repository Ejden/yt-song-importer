package com.adrianstypinski.ytsongimporter.authorization;

import com.adrianstypinski.ytsongimporter.exceptions.NoAttributesProvidedException;
import com.adrianstypinski.ytsongimporter.exceptions.UserNotFoundException;
import com.adrianstypinski.ytsongimporter.payload.SpotifyTokenResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Controller
@RequestMapping("auth")
public class AuthorizationController {

    private final SpotifyAuthorizationService spotifyAuthService;
    private final GoogleAuthorizationService googleAuthorizationService;

    public AuthorizationController(
            SpotifyAuthorizationService spotifyAuthService,
            GoogleAuthorizationService googleAuthorizationService) {

        this.spotifyAuthService = spotifyAuthService;
        this.googleAuthorizationService = googleAuthorizationService;
    }

    // === SPOTIFY AUTHORIZATION ===
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
    public String acceptSpotifyCode(HttpServletResponse response, @RequestParam String code, HttpSession session) throws UserNotFoundException {
        SpotifyTokenResponse spotifyResponse = spotifyAuthService.getAuthorizationTokenBySecret(code, session);

        Cookie accessToken = new Cookie("spotifyAccessToken", spotifyResponse.getAccess_token());
        accessToken.setHttpOnly(true);
        accessToken.setMaxAge(spotifyResponse.getExpires_in());

        response.addCookie(accessToken);

        return "dashboard";
    }

    // === GOOGLE AUTHORIZATION ===
    @GetMapping("youtube/requestCode")
    public RedirectView getYoutubeAuth() throws GeneralSecurityException, IOException {
        return googleAuthorizationService.getAuthorizationCode();
    }

    @GetMapping("youtube/acceptAuthCode")
    public String acceptYoutubeCode(HttpServletResponse response, @RequestParam String code, HttpSession session) {
        return googleAuthorizationService.acceptGoogleAuthCode(response, code, session);
    }
}
