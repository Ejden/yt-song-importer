package com.adrianstypinski.ytsongimporter.authorization.google;

public enum GoogleScope {
    YOUTUBE_READ_ONLY("https://www.googleapis.com/auth/youtube.readonly");

    private final String scope;

    GoogleScope(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }
}
