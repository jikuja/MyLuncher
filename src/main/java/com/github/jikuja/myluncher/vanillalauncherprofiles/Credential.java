package com.github.jikuja.myluncher.vanillalauncherprofiles;

import com.google.gson.annotations.Expose;

// pojo for credentials. Not good, does not work with loadFromStorage or saveForStorage
// also missing support for nested objects(= user properties)

public class Credential {
    @Expose
    private String accessToken;
    @Expose
    private String userid;
    @Expose
    private String username;
    @Expose
    private String displayName;
    @Expose
    private String uuid;
}
