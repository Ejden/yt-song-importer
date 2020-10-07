package com.adrianstypinski.ytsongimporter;

public enum Scope {
    // === IMAGES ===
    UGC_IMAGE_UPLOAD("ugc-image-upload"),

    // === PLAYLISTS ===
    PLAYLIST_READ_COLLABORATIVE("playlist-read-collaborative"),
    PLAYLIST_MODIFY_PRIVATE("playlist-modify-private"),
    PLAYLIST_MODIFY_PUBLIC("playlist-modify-public"),
    PLAYLIST_READ_PRIVATE("playlist-read-private"),

    // === LISTENING HISTORY ===
    USER_READ_PLAYBACK_POSITION("user-read-playback-position"),
    USER_READ_RECENTLY_PLAYED("user-read-recently-played"),
    USER_TOP_READ("user-top-read"),

    // === SPOTIFY CONNECT ===
    USER_MODIFY_PLAYBACK_STATE("user-modify-playback-state"),
    USER_READ_CURRENTLY_PLAYING("user-read-currently-playing"),
    USER_READ_PLAYBACK_STATE("user-read-playback-state"),

    // === USERS ===
    USER_READ_PRIVATE("user-read-private"),
    USER_READ_EMAIL("user-read-email"),

    // === LIBRARY ===
    USER_LIBRARY_MODIFY("user-library-modify"),
    USER_LIBRARY_READ("user-library-read"),

    // === FOLLOW ===
    USER_FOLLOW_MODIFY("user-follow-modify"),
    USER_FOLLOW_READ("user-follow-read"),

    // === PLAYBACK ===
    STREAMING("streaming"),
    APP_REMOTE_CONTROL("app-remote-control");



    private final String scope;

    Scope(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }
}
