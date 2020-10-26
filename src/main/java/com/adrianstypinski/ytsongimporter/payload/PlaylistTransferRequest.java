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
    private String youtubePlaylistId;
    private String youtubePlaylistName;
//    private String youtubePlaylistCreatorChannelId;
//    private String youtubePlaylistCreatorChannelTitle;3
//    private String spotifyPlaylistExternalUrls;

}
