package com.github.jikuja.myluncher;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.google.common.collect.Lists;
import net.feed_the_beast.launcher.json.JsonFactory;
import net.feed_the_beast.launcher.json.versions.Library;
import net.feed_the_beast.launcher.json.versions.Version;
import net.ftb.data.CommandLineSettings;
import net.ftb.data.LoginResponse;
import net.ftb.data.Settings;
import net.ftb.data.UserManager;
import net.ftb.log.*;
import net.ftb.minecraft.MCInstaller;
import net.ftb.minecraft.MCLauncher;
import net.ftb.util.OSUtils;
import net.ftb.workers.AuthlibDLWorker;
import net.ftb.workers.LoginWorker;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    private static JCommander jc;
    private static UserManager userManager;
    private static LoginResponse resp;

    public static void main(String[] args) {
        initFTBLauncher(args);

        // handle auth
        String user = CommandLineSettings.getSettings().getUsername();
        if (user != null) {
            handleAuth(CommandLineSettings.getSettings().getUsername());
        } else {
            Logger.logError("No username given");
            return;
        }

        // handle pack && handle launch
        MCInstaller.launchMinecraft(Settings.getSettings().getInstallPath(), CommandLineSettings.getSettings().getPackDir(), CommandLineSettings.getSettings().getMcVersion(), resp, false);
    }

    public static void initFTBLauncher(String[] args) {
        try {
            jc = new JCommander(CommandLineSettings.getSettings(), args);
        } catch (ParameterException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        if (CommandLineSettings.getSettings().isHelp()) {
            jc.setProgramName("MyLunch.jar");
            jc.usage();
            System.exit(0);
        }

        int logLevel = CommandLineSettings.getSettings().getVerbosity();
        LogLevel stdoutLogLevel = LogLevel.values()[logLevel];
        LogSource stdoutLogSource = CommandLineSettings.getSettings().isMcLogs() ? LogSource.ALL : LogSource.LAUNCHER;

        Logger.addListener(new StdOutLogger(stdoutLogLevel, stdoutLogSource));

        AuthlibDLWorker authworker = new AuthlibDLWorker(OSUtils.getDynamicStorageLocation() + File.separator + "authlib" + File.separator, "1.5.17");
        // make it faster because we don't really need to check and download authlib
        //authworker.doInBackground();
        authworker.addToClasspath(new File(OSUtils.getDynamicStorageLocation() + File.separator + "authlib" + File.separator) + File.separator + "authlib-" + "1.5.17" + ".jar");

        // this is slow and should be done in background thread!
        userManager = new UserManager(new File(OSUtils.getCacheStorageLocation(), "logindata"), new File(OSUtils.getDynamicStorageLocation(), "logindata"));
    }

    public static void handleAuth(String account) {
        doLogin(UserManager.getUsername(account), UserManager.getPassword(account),
                UserManager.getMojangData(account), UserManager.getName(account));
    }

    private static void doLogin (final String username, String password, String mojangData, String selectedProfile) {
        LoginWorker loginWorker = new LoginWorker(username, password, mojangData, selectedProfile);
        String ret = loginWorker.doInBackground();
        resp = loginWorker.getResp();
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
    }
}
