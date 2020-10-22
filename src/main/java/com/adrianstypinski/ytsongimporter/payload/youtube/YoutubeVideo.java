package com.adrianstypinski.ytsongimporter.payload.youtube;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class YoutubeVideo {
    @SerializedName("kind")
    private String kind;
    @SerializedName("etag")
    private String etag;
    @SerializedName("id")
    private String id;
    @SerializedName("snippet")
    private Snippet snippet;

    @Data
    public static class Snippet {
        @SerializedName("title")
        private String title;
        @SerializedName("description")
        private String description;
    }
}
