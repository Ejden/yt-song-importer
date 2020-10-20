package com.adrianstypinski.ytsongimporter.payload.youtube;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YoutubePlaylistItemDto {
    private String channelId;
    private String channelTitle;
    private String playlistId;
    private String playlistTitle;
}
