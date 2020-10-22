package com.adrianstypinski.ytsongimporter;

import com.adrianstypinski.ytsongimporter.model.User;
import com.adrianstypinski.ytsongimporter.model.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class ViewController {
    private final PlaylistsTransferService playlistsTransferService;
    private final UserService userService;

    @Autowired
    public ViewController(PlaylistsTransferService playlistsTransferService, UserService userService) {
        this.playlistsTransferService = playlistsTransferService;
        this.userService = userService;
    }

    @GetMapping("dashboard")
    public String dashboard(@RequestParam String code) {
        return "dashboard/dashboard";
    }

    @GetMapping
    public String getStarted(Model model, HttpSession session) {

        User user = (User) session.getAttribute(User.ATTRIBUTE_NAME);

        if (user == null) {
            user = new User();
        }

        session.setAttribute(User.ATTRIBUTE_NAME, user);
        model.addAttribute(User.ATTRIBUTE_NAME, user.toUserDto());

        return "dashboard/dashboard";
    }

    @GetMapping("me/transfer")
    public String showTransferView(HttpSession session, Model model) {
        return playlistsTransferService.getTransferView(session, model);
    }

    @GetMapping("me/summary")
    public String showSummaryView(HttpSession session, Model model) {
        return playlistsTransferService.getSummaryView(session, model);
    }
}
