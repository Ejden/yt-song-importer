package com.adrianstypinski.ytsongimporter.authorization;

import com.adrianstypinski.ytsongimporter.Scope;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Set;

import static com.adrianstypinski.ytsongimporter.Scope.*;
import static com.adrianstypinski.ytsongimporter.Scope.USER_READ_PRIVATE;

@Service
public class SpotifyAuthorizationService implements AuthorizationService {

    private final String spotifyAuthUrl = "https://accounts.spotify.com/authorize";

    public RedirectView getAuthorization(ModelMap model) {
        Set<Scope> scopes = Set.of(
            USER_LIBRARY_READ,
            USER_READ_PRIVATE,
            USER_MODIFY_PLAYBACK_STATE
        );

        RedirectView redirectView = new RedirectView(spotifyAuthUrl);

        redirectView.addStaticAttribute("response_type", "code");
        redirectView.addStaticAttribute("scope", prepareScopeAttribute(scopes));
        redirectView.addStaticAttribute("client_id", System.getenv("SPOTIFY_CLIENT_ID"));
        redirectView.addStaticAttribute("redirect_uri", "https://google.com");

        return redirectView;
    }

    private String prepareScopeAttribute(Set<Scope> scopes) {
        // Adding all scopes to String
        return scopes.stream().map(Scope::getScope).reduce((a, b) -> a + " " + b).get();
    }
}
