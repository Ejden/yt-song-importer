package com.adrianstypinski.ytsongimporter.payload.spotify;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SpotifyUserDetails {
    @SerializedName("id")
    private String id;
}
