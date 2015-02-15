package com.github.jikuja.myluncher.utils;

import java.io.File;

public class Utils {
    public static enum OS {
        WINDOWS, LINUX, MACOSX, OTHER,
    }

    public static OS  getRunningOS() {
        String s = System.getProperty("os.name").toLowerCase();
        if (s.contains("windows")) {
            return OS.WINDOWS;
        } else if (s.contains("linux")) {
            return OS.LINUX;
        } else if (s.contains("mac os x")) {
            return OS.MACOSX;
        } else {
            return OS.OTHER;
        }
    }

    public static String getVanillaFolder() {
        String home = System.getProperty("user.home");
        OS os = getRunningOS();
        switch (os) {
        case WINDOWS:
            return home + File.separator + "AppData" + File.separator + "Roaming" + File.separator + ".minecraft" + File.separator;
        case LINUX:
            return home + File.separator + ".minecraft" + File.separator;
        case MACOSX:
            return home + File.separator + ".minecraft" + File.separator;
        case OTHER:
            return home + File.separator + ".minecraft" + File.separator;
        default:
            // just crash, should not happen
            return null;

        }
    }
}
