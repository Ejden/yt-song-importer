package com.adrianstypinski.ytsongimporter.authorization;

import com.adrianstypinski.ytsongimporter.model.User;
import com.adrianstypinski.ytsongimporter.model.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Set;

@Service
@Slf4j
public class GoogleAuthorizationService {

    private final GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow;
    private final UserService userService;

    @Autowired
    public GoogleAuthorizationService(UserService userService) throws GeneralSecurityException, IOException {
        this.googleAuthorizationCodeFlow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                System.getenv("YOUTUBE_CLIENT_ID"),
                System.getenv("YOUTUBE_CLIENT_SECRET"),
                getScopes()
        ).build();

        this.userService = userService;
    }

    public RedirectView getAuthorizationCode() throws GeneralSecurityException, IOException {
        return new RedirectView(
                googleAuthorizationCodeFlow.newAuthorizationUrl()
                        .setRedirectUri(System.getenv("YOUTUBE_REDIRECT_URI"))
                        .toString()
        );
    }

    private Set<String> getScopes() {
        return Set.of(
                GoogleScope.YOUTUBE_READ_ONLY.getScope()
        );
    }

    public String acceptGoogleAuthCode(HttpServletResponse response, String authCode, HttpSession session) {
        User user = (User) session.getAttribute(User.ATTRIBUTE_NAME);

        if (user != null) {
            user.setGoogleAuthCode(authCode);

            GoogleAuthorizationCodeTokenRequest tokenRequest = googleAuthorizationCodeFlow.newTokenRequest(authCode);
            tokenRequest.setRedirectUri(System.getenv("YOUTUBE_REDIRECT_URI"));

            try {
                // Getting token from Google API
                GoogleTokenResponse token = tokenRequest.execute();

                user.setGoogleToken(token);

                // Updating user
                userService.saveUser(user);
                session.setAttribute(User.ATTRIBUTE_NAME, user);

                log.info("Updated user {}", user.getId());

                return "dashboard";
            } catch (IOException e) {
                log.error("There was an error while sending google token request. \n {}", e.getMessage());
                return "error";
            }
        } else {
            log.error("User not found");
            return "error";
        }
    }
}
