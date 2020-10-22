package com.adrianstypinski.ytsongimporter;

import com.adrianstypinski.ytsongimporter.exceptions.UserNotFoundException;
import com.adrianstypinski.ytsongimporter.payload.PlaylistTransferRequest;
import com.adrianstypinski.ytsongimporter.payload.youtube.YoutubeVideo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collection;

@RestController
@Slf4j
public class ApiController {

    private final PlaylistsTransferService transferService;

    public ApiController(PlaylistsTransferService transferService) {
        this.transferService = transferService;
    }

    @PutMapping("me/transfer/submit")
    public Collection<YoutubeVideo> updatePlaylist(HttpSession session, @RequestBody PlaylistTransferRequest requestBody) throws UserNotFoundException {
        return transferService.transferToExistingPlaylist(session, requestBody);
    }

    @PostMapping("me/transfer/submit")
    public Collection<YoutubeVideo> createNewPlaylist(HttpSession session, @RequestBody PlaylistTransferRequest requestBody) throws UserNotFoundException {
        return transferService.transferToNewPlaylist(session, requestBody);
    }

    @GetMapping("me/transfer/videos/error")
    public Collection<YoutubeVideo> getNotTransferredVideos(HttpSession session) {
        return transferService.getNotTransferredVideos(session);
    }
}
