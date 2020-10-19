package com.adrianstypinski.ytsongimporter.payload;

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
