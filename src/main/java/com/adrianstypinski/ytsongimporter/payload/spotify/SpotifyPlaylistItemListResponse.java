package com.adrianstypinski.ytsongimporter.payload.spotify;

import com.adrianstypinski.ytsongimporter.payload.spotify.PlaylistItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpotifyPlaylistItemListResponse {
    private String href;
    private List<PlaylistItem> items;
}
