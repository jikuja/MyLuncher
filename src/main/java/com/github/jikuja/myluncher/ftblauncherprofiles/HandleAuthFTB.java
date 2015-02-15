package com.github.jikuja.myluncher.ftblauncherprofiles;

import net.ftb.data.LoginResponse;
import net.ftb.data.UserManager;
import net.ftb.log.Logger;
import net.ftb.util.OSUtils;
import net.ftb.workers.LoginWorker;

import java.io.File;
import java.io.IOException;

public class HandleAuthFTB {
    private static UserManager userManager;

    public static LoginResponse handleAuth(String account, boolean dryrun) {
        if (dryrun) {
            Logger.logError("--dry-run not supported yet. Aborting auth");
            return null;
        }

        // init UserManager
        userManager = new UserManager(new File(OSUtils.getCacheStorageLocation(), "logindata"), new File(OSUtils.getDynamicStorageLocation(), "logindata"));

        return doLogin(UserManager.getUsername(account), UserManager.getPassword(account),
                UserManager.getMojangData(account), UserManager.getName(account));
    }

    private static LoginResponse doLogin (final String username, String password, String mojangData, String selectedProfile) {
        LoginWorker loginWorker = new LoginWorker(username, password, mojangData, selectedProfile);
        String ret = loginWorker.doInBackground();
        LoginResponse resp = loginWorker.getResp();
        Logger.logDebug("loginWorker ret: " + ret);

        if (ret.equals("good")) {
            Logger.logInfo("Login complete.");
            try {
                // save userdata, including new mojangData
                userManager.write();
                Logger.logDebug("user data saved");
            } catch (IOException e) {
                Logger.logError("logindata saving failed!");
            }
        }

        return resp;
    }

}
