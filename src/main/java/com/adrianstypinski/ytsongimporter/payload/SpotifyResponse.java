package com.adrianstypinski.ytsongimporter.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpotifyResponse {
    private String access_token;
    private String token_type;
    private String scope;
    private Integer expires_in;
    private String refresh_token;
}
