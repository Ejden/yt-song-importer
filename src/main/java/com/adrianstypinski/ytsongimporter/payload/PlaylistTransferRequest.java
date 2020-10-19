package com.adrianstypinski.ytsongimporter.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PlaylistTransferRequest {
    private String spotifyPlaylistId;
    private String spotifyPlaylistName;
    private String spotifyPlaylistExternalUrls;
    private String youtubePlaylistId;
    private String youtubePlaylistTitle;
    private String youtubePlaylistCreatorChannelId;
    private String youtubePlaylistCreatorChannelTitle;
}
