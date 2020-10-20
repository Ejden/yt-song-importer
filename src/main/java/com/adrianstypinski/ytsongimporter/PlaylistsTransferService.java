package com.adrianstypinski.ytsongimporter;

import com.adrianstypinski.ytsongimporter.model.User;
import com.adrianstypinski.ytsongimporter.payload.*;
import com.adrianstypinski.ytsongimporter.payload.spotify.PlaylistItem;
import com.adrianstypinski.ytsongimporter.payload.spotify.SpotifyUserDetails;
import com.adrianstypinski.ytsongimporter.payload.youtube.YoutubePlaylistItemDto;
import com.adrianstypinski.ytsongimporter.payload.youtube.YoutubePlaylistItemListResponse;
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

            // Get user playlists from Youtube
            Set<YoutubePlaylistItemDto> youtubePlaylists = youtubeService.getPlaylists(user.getGoogleToken().getAccessToken());
            model.addAttribute(YoutubePlaylistItemListResponse.ATTRIBUTE_NAME, youtubePlaylists);

            // Get user playlists from Spotify
            Set<PlaylistItem> spotifyPlaylists = spotifyService.getPlaylists(user.getSpotifyToken().getAccess_token());
            model.addAttribute("SPOTIFY_PLAYLISTS", spotifyPlaylists);

            // Get user details from Spotify and save it in session
            SpotifyUserDetails userDetails = spotifyService.getUserDetails(user.getSpotifyToken().getAccess_token());
            user.setSpotifyUserDetails(userDetails);
            session.setAttribute(User.ATTRIBUTE_NAME, user);

            return "playlists/playlists";
        } else {
            log.info("User not found. Couldn't generate transfer view");
            return "error";
        }
    }

    public Collection<YoutubePlaylistItemDto> transferToNewPlaylist(HttpSession session, PlaylistTransferRequest request) {
        User user = (User) session.getAttribute(User.ATTRIBUTE_NAME);

        if (user != null) {
            spotifyService.createPlaylist(request.getSpotifyPlaylistName(), user.getSpotifyUserDetails().getId(), user.getSpotifyToken());
        }
        return null;
    }

    public Collection<YoutubePlaylistItemDto> transferToExistingPlaylist() {
        return null;
    }
}
