package com.adrianstypinski.ytsongimporter.payload.youtube;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class YoutubePlaylistItems {
    @SerializedName("kind")
    private String kind;
    @SerializedName("etag")
    private String etag;
    @SerializedName("nextPageToken")
    private String nextPageToken;

    @SerializedName("items")
    private List<YoutubeVideo> items;
}
