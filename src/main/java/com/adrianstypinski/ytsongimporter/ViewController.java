package com.adrianstypinski.ytsongimporter;

import com.adrianstypinski.ytsongimporter.model.User;
import com.adrianstypinski.ytsongimporter.model.UserBuilder;
import com.adrianstypinski.ytsongimporter.model.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Controller
public class ViewController {
    private final SpotifyService spotifyService;
    private final YoutubeService youtubeService;
    private final PlaylistsTransferService playlistsTransferService;
    private final UserService userService;

    @Autowired
    public ViewController(SpotifyService spotifyService, YoutubeService youtubeService, PlaylistsTransferService playlistsTransferService, UserService userService) {
        this.spotifyService = spotifyService;
        this.youtubeService = youtubeService;
        this.playlistsTransferService = playlistsTransferService;
        this.userService = userService;
    }

    @GetMapping("dashboard")
    public String dashboard(@RequestParam String code) {
        return "dashboard/dashboard";
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
            model.addAttribute(User.ATTRIBUTE_NAME, user.toUserDto());
            session.setAttribute("USER", user);
        }

        return "dashboard/dashboard";
    }

    @GetMapping("me/transfer")
    public String showTransferView(HttpSession session, Model model) {
        return playlistsTransferService.getTransferView(session, model);
    }
}
