package com.adrianstypinski.ytsongimporter;

import com.adrianstypinski.ytsongimporter.model.User;
import com.adrianstypinski.ytsongimporter.model.UserBuilder;
import com.adrianstypinski.ytsongimporter.model.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Controller
public class MainController {
    private final SpotifyService spotifyService;
    private final YoutubeService youtubeService;
    private final UserService userService;

    @Autowired
    public MainController(SpotifyService spotifyService, YoutubeService youtubeService, UserService userService) {
        this.spotifyService = spotifyService;
        this.youtubeService = youtubeService;
        this.userService = userService;
    }

    @GetMapping("dashboard")
    public String dashboard(@RequestParam String code) {
        return "dashboard";
    }

    @GetMapping("")
    public String getStarted(Model model, HttpSession session) {

        User user = (User) session.getAttribute("USER");

        if (user == null) {
            try {
                user = UserBuilder.getUserWithCodeVerifierAndCodeChallenge();
                userService.saveUser(user);
            } catch (NoSuchAlgorithmException e) {
                log.error(e.getMessage());
            }
        }

        if (user != null) {
            model.addAttribute(user);
            session.setAttribute("USER", user);
        }

        return "dashboard/dashboard";
    }

    @GetMapping("youtube/me/playlists")
    public ResponseEntity<String> getPlaylists(HttpSession session) {
        User user = (User) session.getAttribute(User.ATTRIBUTE_NAME);
        return youtubeService.getPlaylists(user.getGoogleToken().getAccessToken());
    }
}
