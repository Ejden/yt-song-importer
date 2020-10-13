package com.adrianstypinski.ytsongimporter.model;

import java.security.NoSuchAlgorithmException;

public abstract class UserBuilder {

    public static User getUserWithCodeVerifierAndCodeChallenge() throws NoSuchAlgorithmException {
        return new User();

    }
}
