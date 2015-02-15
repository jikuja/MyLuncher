package com.github.jikuja.myluncher.vanillalauncherprofiles;

import com.google.gson.annotations.Expose;

public class Profile {
    @Expose
    private String name;

    @Expose
    private Resolution resolution;

    @Expose
    private String launcherVisibilityOnGameClose;

    @Expose
    private String lastVersionId;

    @Expose
    private Boolean useHopperCrashService;
}
