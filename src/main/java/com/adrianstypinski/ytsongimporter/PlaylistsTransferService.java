package com.adrianstypinski.ytsongimporter;

import com.adrianstypinski.ytsongimporter.exceptions.UserNotFoundException;
import com.adrianstypinski.ytsongimporter.model.User;
import com.adrianstypinski.ytsongimporter.payload.*;
import com.adrianstypinski.ytsongimporter.payload.spotify.SpotifyPlaylist;
import com.adrianstypinski.ytsongimporter.payload.spotify.SpotifyTrack;
import com.adrianstypinski.ytsongimporter.payload.spotify.SpotifyUserDetails;
import com.adrianstypinski.ytsongimporter.payload.youtube.YoutubePlaylistItemDto;
import com.adrianstypinski.ytsongimporter.payload.youtube.YoutubePlaylistItemListResponse;
import com.adrianstypinski.ytsongimporter.payload.youtube.YoutubeVideo;
import com.adrianstypinski.ytsongimporter.utils.YoutubeTitleParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.HashSet;
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
            Set<SpotifyPlaylist> spotifyPlaylists = spotifyService.getPlaylists(user.getSpotifyToken());
            model.addAttribute("SPOTIFY_PLAYLISTS", spotifyPlaylists);

            // Get user details from Spotify and save it in session
            SpotifyUserDetails userDetails = spotifyService.getUserDetails(user.getSpotifyToken());
            user.setSpotifyUserDetails(userDetails);
            session.setAttribute(User.ATTRIBUTE_NAME, user);

            return "playlists/playlists";
        } else {
            log.info("User not found. Couldn't generate transfer view");
            return "error";
        }
    }

    public Collection<YoutubeVideo> transferToNewPlaylist(HttpSession session, PlaylistTransferRequest request) throws UserNotFoundException {
        User user = (User) session.getAttribute(User.ATTRIBUTE_NAME);

        if (user != null) {
            SpotifyPlaylist playlist = spotifyService.createPlaylist(request.getSpotifyPlaylistName(), user.getSpotifyUserDetails().getId(), user.getSpotifyToken());
            return transferVideos(user, request, playlist);
        } else {
            throw new UserNotFoundException();
        }
    }

    public Collection<YoutubeVideo> transferToExistingPlaylist(HttpSession session, PlaylistTransferRequest request) throws UserNotFoundException {
        User user = (User) session.getAttribute(User.ATTRIBUTE_NAME);

        if (user != null) {
            SpotifyPlaylist playlist = spotifyService.getPlaylist(user.getSpotifyToken(), request.getSpotifyPlaylistId());
            return transferVideos(user, request, playlist);
        } else {
            throw new UserNotFoundException();
        }
    }

    private Collection<YoutubeVideo> transferVideos(User user, PlaylistTransferRequest request, SpotifyPlaylist playlist) {
        Set<YoutubeVideo> youtubeVideos = youtubeService.getVideos(request.getYoutubePlaylistId(), user.getGoogleToken());

        Set<SpotifyTrack> foundTracks = new HashSet<>();
        Set<YoutubeVideo> notFoundTracks = new HashSet<>();

        // Getting tracks from spotify based on youtube titles
        youtubeVideos.forEach(youtubeVideo -> {
            String trackTitle = YoutubeTitleParser.parseTitle(youtubeVideo.getSnippet().getTitle());
            SpotifyTrack spotifyTrack = spotifyService.searchForTrack(user.getSpotifyToken(), trackTitle);

            if (spotifyTrack != null) {
                foundTracks.add(spotifyTrack);
            } else {
                notFoundTracks.add(youtubeVideo);
            }
        });

        // Add foundTracks to new created spotify playlist
        foundTracks.forEach(track -> spotifyService.addTrackToPlaylist(user.getSpotifyToken(), playlist.getId(), track.getUri()));

        return notFoundTracks;
    }
}
