package com.adrianstypinski.ytsongimporter;

import com.adrianstypinski.ytsongimporter.model.User;
import com.adrianstypinski.ytsongimporter.payload.YoutubePlaylistItemDto;
import com.adrianstypinski.ytsongimporter.payload.YoutubePlaylistItemListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Set;

@Service
@Slf4j
public class PlaylistsTransferService {
    private final SpotifyService spotifyService;
    private final YoutubeService youtubeService;

    public PlaylistsTransferService(SpotifyService spotifyService, YoutubeService youtubeService) {
        this.spotifyService = spotifyService;
        this.youtubeService = youtubeService;
    }

    public String getTransferView(HttpSession session, Model model) {
        User user = (User) session.getAttribute(User.ATTRIBUTE_NAME);
        if (user != null) {

            Set<YoutubePlaylistItemDto> youtubePlaylists = youtubeService.getPlaylists(user.getGoogleToken().getAccessToken());
            model.addAttribute(YoutubePlaylistItemListResponse.ATTRIBUTE_NAME, youtubePlaylists);

            model.addAttribute("SPOTIFY_PLAYLISTS", spotifyService.getPlaylists(user.getSpotifyToken().getAccess_token()));

            return "playlists/playlists";
        } else {
            log.info("User not found. Couldn't generate transfer view");
            return "error";
        }
    }

    public Collection<YoutubePlaylistItemDto> transferToNewPlaylist() {
        return null;
    }

    public Collection<YoutubePlaylistItemDto> transferToExistingPlaylist() {
        return null;
    }
}
