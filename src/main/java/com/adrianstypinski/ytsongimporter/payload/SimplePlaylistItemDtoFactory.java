package com.adrianstypinski.ytsongimporter.payload;

public abstract class SimplePlaylistItemDtoFactory {

    public static YoutubePlaylistItemDto toPlaylistItemDto(YoutubePlaylistItemListResponse.ItemsBean item) {
        String channelId = item.getSnippet().getChannelId();
        String channelTitle = item.getSnippet().getChannelTitle();
        String playlistId = item.getId();
        String playlistTitle = item.getSnippet().getTitle();

        return YoutubePlaylistItemDto.builder()
                .channelId(channelId)
                .channelTitle(channelTitle)
                .playlistId(playlistId)
                .playlistTitle(playlistTitle)
                .build();
    }
}
