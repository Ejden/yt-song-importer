package com.adrianstypinski.ytsongimporter.authorization.spotify;

import com.adrianstypinski.ytsongimporter.exceptions.NoAttributesProvidedException;
import com.adrianstypinski.ytsongimporter.exceptions.UserNotFoundException;
import com.adrianstypinski.ytsongimporter.model.User;
import com.adrianstypinski.ytsongimporter.model.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.Base64;
import java.util.Optional;
import java.util.Set;

import static com.adrianstypinski.ytsongimporter.authorization.spotify.SpotifyScope.*;
import static com.adrianstypinski.ytsongimporter.authorization.spotify.SpotifyAuthorizationService.Attribute.*;

@Service
@Slf4j
public class SpotifyAuthorizationService {

    private final String spotifyAuthUrl = "https://accounts.spotify.com/authorize";
    private final String spotifyTokenAuthUrl = "https://accounts.spotify.com/api/token";

    private final UserService userService;

    @Autowired
    public SpotifyAuthorizationService(UserService userService) {
        this.userService = userService;
    }

    public RedirectView getAuthorizationCode() throws NoAttributesProvidedException {
        Set<SpotifyScope> scopes = Set.of(
                PLAYLIST_READ_COLLABORATIVE,
                PLAYLIST_READ_PRIVATE,
                PLAYLIST_MODIFY_PRIVATE,
                PLAYLIST_MODIFY_PUBLIC
        );

        return getRedirectViewToSpotifyCode(scopes);
    }

    public SpotifyTokenResponse getAuthorizationTokenBySecret(String authCode, HttpSession session, Model model) throws UserNotFoundException {
        // Getting user from session
        User user = (User) session.getAttribute(User.ATTRIBUTE_NAME);

        if (user != null) {
            // Save user
            user.setSpotifyAuthCode(authCode);

            // Object that creates requests
            RestTemplate restTemplate = new RestTemplate();
            // Headers for request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // Adding authorization encoded client_id and client_secret to headers
            headers.add("Authorization", getAuthorizationAttribute());

            // Creating request based on body and headers
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(getBodyForAuthTokenRequest(user), headers);

            // Getting response from Spotify API
            log.info("Getting response from Spotify API for {}", user);
            SpotifyTokenResponse spotifyResponse = restTemplate.postForObject(spotifyTokenAuthUrl, request, SpotifyTokenResponse.class);

            user.setSpotifyToken(spotifyResponse);
            user.setAuthorizedOnSpotify(true);

            userService.saveUser(user);

            session.setAttribute(User.ATTRIBUTE_NAME, user);
            model.addAttribute(User.ATTRIBUTE_NAME, user.toUserDto());

            log.info("Updated user {}", user.getId());

            return spotifyResponse;
        } else {
            log.error("User for sessionId = {} was not found", session.getId());
            throw new UserNotFoundException();
        }
    }

    private String getAuthorizationAttribute() {
        String spotifyClientId = System.getenv("SPOTIFY_CLIENT_ID");
        String spotifyClientSecret = System.getenv("SPOTIFY_CLIENT_SECRET");

        String authorizationCode = String.format("%s:%s", spotifyClientId, spotifyClientSecret);

        return String.format("Basic %s", Base64.getEncoder().encodeToString(authorizationCode.getBytes()));
    }

    private String prepareScopeAttribute(Set<SpotifyScope> scopes) throws NoAttributesProvidedException {
        // Adding all scopes to String
        Optional<String> attributes = scopes.stream().map(SpotifyScope::getScope).reduce((a, b) -> a + " " + b);
        if (attributes.isPresent()) {
            return attributes.get();
        } else {
            throw new NoAttributesProvidedException();
        }
    }

    private MultiValueMap<String, String> getBodyForAuthTokenRequest(User user) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add(GRANT_TYPE.getAtt(), "authorization_code");
        params.add(AUTHORIZATION_CODE.getAtt(), user.getSpotifyAuthCode());
        params.add(REDIRECT_URI.getAtt(), System.getenv("REDIRECT_URI"));

        return params;
    }

    private RedirectView getRedirectViewToSpotifyCode(Set<SpotifyScope> scopes) throws NoAttributesProvidedException {
        RedirectView redirectView = new RedirectView(spotifyAuthUrl);

        // Adding path variables to url
        redirectView.addStaticAttribute(CLIENT_ID.getAtt(), System.getenv("SPOTIFY_CLIENT_ID"));
        redirectView.addStaticAttribute(RESPONSE_TYPE.getAtt(), "code");
        redirectView.addStaticAttribute(REDIRECT_URI.getAtt(), System.getenv("REDIRECT_URI"));
        redirectView.addStaticAttribute(SCOPE.getAtt(), prepareScopeAttribute(scopes));

        return redirectView;
    }

    @AllArgsConstructor
    @Getter
    enum Attribute {
        RESPONSE_TYPE("response_type"),
        SCOPE("scope"),
        CLIENT_ID("client_id"),
        REDIRECT_URI("redirect_uri"),
        CODE_CHALLENGE_METHOD("code_challenge_method"),
        CODE_CHALLENGE("code_challenge"),
        CODE_VERIFIER("code_verifier"),
        STATE("state"),
        GRANT_TYPE("grant_type"),
        AUTHORIZATION_CODE("code");

        private final String att;
    }
}
