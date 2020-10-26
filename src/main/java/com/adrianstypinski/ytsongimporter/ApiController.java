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

    @PutMapping("me/transfer/spotify/submit")
    public Collection<YoutubeVideo> transferFromYoutubeToExistingPlaylist(HttpSession session, @RequestBody PlaylistTransferRequest requestBody) throws UserNotFoundException {
        return transferService.transferFromYtToExistingPlaylist(session, requestBody);
    }

    @PostMapping("me/transfer/spotify/submit")
    public Collection<YoutubeVideo> transferFromYoutubeToNewPlaylist(HttpSession session, @RequestBody PlaylistTransferRequest requestBody) throws UserNotFoundException {
        return transferService.transferFromYtToNewPlaylist(session, requestBody);
    }

    @PutMapping("me/transfer/youtube/submit")
    public Collection<String> transferFromSpotifyToExistingPlaylist(HttpSession session, @RequestBody PlaylistTransferRequest requestBody) throws UserNotFoundException {
        return null;
    }

    @PostMapping("me/transfer/youtube/submit")
    public Collection<String> transferFromSpotifyToNewPlaylist(HttpSession session, @RequestBody PlaylistTransferRequest requestBody) throws UserNotFoundException {
        return null;
    }

    @GetMapping("me/transfer/videos/error")
    public Collection<YoutubeVideo> getNotTransferredVideos(HttpSession session) {
        return transferService.getNotTransferredVideos(session);
    }
}
