package com.adrianstypinski.ytsongimporter.payload.youtube;

import com.fasterxml.jackson.annotation.JsonAlias;
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
        @SerializedName("thumbnails")
        private Thumbnails thumbnails;
        @SerializedName("resourceId")
        private ResourceId resourceId;

        @Data
        public static class Thumbnails {
            @SerializedName("default")
            @JsonAlias("default")
            private Thumbnail small;
            @SerializedName("medium")
            private Thumbnail medium;

            @Data
            public static class Thumbnail {
                @SerializedName("url")
                private String url;
                @SerializedName("width")
                private int width;
                @SerializedName("height")
                private int height;
            }
        }

        @Data
        public static class ResourceId {
            @SerializedName("videoId")
            private String videoId;
        }
    }
}
