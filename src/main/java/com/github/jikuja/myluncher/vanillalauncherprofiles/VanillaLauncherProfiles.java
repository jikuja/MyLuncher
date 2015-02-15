package com.github.jikuja.myluncher.vanillalauncherprofiles;

import com.google.gson.annotations.Expose;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

public class VanillaLauncherProfiles {
    @Expose
    public Map<String, Profile> profiles;

    @Expose
    public String selectedProfile;

    @Expose
    @Getter
    public UUID clientToken;

    @Expose
    @Getter
    public AuthenticationDatabase authenticationDatabase;
    // this was bad idea but code is still there. Simnple pojo works but NO
    //@Expose
    //@SerializedName("authenticationDatabase")
    //public Map<String, Credential> credentials;

    @Expose
    public String selectedUser;
    @Expose
    public LauncherVersion launcherVersion;
}
