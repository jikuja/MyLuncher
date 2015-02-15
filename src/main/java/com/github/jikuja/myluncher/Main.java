package com.github.jikuja.myluncher;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.github.jikuja.myluncher.ftblauncherprofiles.HandleAuthFTB;
import com.github.jikuja.myluncher.vanillalauncherprofiles.HandleAuthVanilla;
import net.ftb.data.CommandLineSettings;
import net.ftb.data.LoginResponse;
import net.ftb.data.Settings;
import net.ftb.log.*;
import net.ftb.minecraft.MCInstaller;
import net.ftb.util.OSUtils;
import net.ftb.workers.AuthlibDLWorker;

import java.io.*;

public class Main {
    private static JCommander jc;
    private static LoginResponse resp = null;


    public static void main(String[] args) {
        initFTBLauncher(args);

        // handle auth
        String user = CommandLineSettings.getSettings().getUsername();
        Boolean dryrun = CommandLineSettings.getSettings().isDryRun();
        if (user != null) {
            // Design pattern needed here?
            if (!CommandLineSettings.getSettings().isVanillaCredential()) {
                resp = HandleAuthFTB.handleAuth(user, dryrun);
            } else {
                resp = HandleAuthVanilla.handleAuth(user, dryrun);
            }

        } else {
            Logger.logError("No username given");
            return;
        }
        if (dryrun) {
            Logger.logInfo("--dry-run given. Aborting now");
            return;
        }
        if (resp == null) {
            Logger.logError("Auth failed");
            return;
        }

        // handle pack && handle launch
        if (CommandLineSettings.getSettings().getPackDir() != null) {
            MCInstaller.launchMinecraft(Settings.getSettings().getInstallPath(), CommandLineSettings.getSettings().getPackDir(), CommandLineSettings.getSettings().getMcVersion(), resp, false);
        }
    }

    public static void initFTBLauncher(String[] args) {
        // Settings class is automatically initilized

        // Setup modified command line arguments
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

        // setup basic logging to stdout. Use --log-mc to show MC logs
        int logLevel = CommandLineSettings.getSettings().getVerbosity();
        LogLevel stdoutLogLevel = LogLevel.values()[logLevel];
        LogSource stdoutLogSource = CommandLineSettings.getSettings().isMcLogs() ? LogSource.ALL : LogSource.LAUNCHER;

        Logger.addListener(new StdOutLogger(stdoutLogLevel, stdoutLogSource));

        // inject authlibworker to classpath
        // TODO write  own, current code spawn harmless Exception
        AuthlibDLWorker authworker = new AuthlibDLWorker(OSUtils.getDynamicStorageLocation() + File.separator + "authlib" + File.separator, "1.5.17");
        // make it faster because we don't really need to check and download authlib
        //authworker.doInBackground();
        authworker.addToClasspath(new File(OSUtils.getDynamicStorageLocation() + File.separator + "authlib" + File.separator) + File.separator + "authlib-" + "1.5.17" + ".jar");
    }
}
