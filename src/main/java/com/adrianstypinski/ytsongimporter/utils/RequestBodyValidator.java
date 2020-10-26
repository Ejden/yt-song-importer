package com.adrianstypinski.ytsongimporter.utils;

import com.adrianstypinski.ytsongimporter.payload.PlaylistTransferRequest;

public class RequestBodyValidator {

    public static boolean validateRequest(Service transferFrom, Service transferTo, PlaylistTransferRequest request) {
        return true;
    }

    private static boolean validateFromYtToSpotify() {
        return true;
    }


    public enum Service {
        SPOTIFY, YOUTUBE
    }
}
