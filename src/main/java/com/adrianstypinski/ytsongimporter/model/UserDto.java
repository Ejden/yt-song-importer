package com.adrianstypinski.ytsongimporter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDto {
    public static final String ATTRIBUTE_NAME = "USER";

    private boolean authorizedOnSpotify = false;
    private boolean authorizedOnYouTube = false;
}
