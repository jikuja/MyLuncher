package com.github.jikuja.myluncher.vanillalauncherprofiles;

import com.github.jikuja.myluncher.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.ftb.data.LoginResponse;
import net.ftb.log.Logger;

import java.io.*;
import java.net.Proxy;
import java.util.Map;
import java.util.UUID;

public class HandleAuthVanilla {
    private static Gson gson = new GsonBuilder()
            // hack for GSON inheritance problem.
            .registerTypeAdapter(AuthenticationDatabase.class, new AuthenticationDatabase.Serializer())
            .setPrettyPrinting()
            .create();

    public static LoginResponse handleAuth(String account, boolean dryrun) {
        BufferedReader br;
        BufferedWriter wr;
        LoginResponse resp = null;

        // fetch from launcher_profiles.json
        try {
            br = new BufferedReader(new FileReader(Utils.getVanillaFolder() + "launcher_profiles.json"));
        } catch (Exception e) {
            Logger.logError("", e);
            return null;
        }

        VanillaLauncherProfiles vanillaLauncerProfiles = gson.fromJson(br, VanillaLauncherProfiles.class);
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Logger.logDebug("FROM .json:\n" + gson.toJson(vanillaLauncerProfiles));

        // do authentication
        if (!dryrun) {
            AuthenticationDatabase db = vanillaLauncerProfiles.getAuthenticationDatabase();
            YggdrasilUserAuthentication authentication = HandleAuthVanilla.callAuthLib(db.getByUserName(account), vanillaLauncerProfiles.getClientToken());
            resp = new LoginResponse(Integer.toString(authentication.getAgent().getVersion()), "token", authentication.getSelectedProfile().getName(),
                    authentication.getAuthenticatedToken(), authentication.getSelectedProfile().getId().toString(), authentication);
        } else {
            return null;
        }

        // save new data from authlib to vanilla launcher profiles
        vanillaLauncerProfiles.getAuthenticationDatabase().setByUserName(account, resp.getAuth().saveForStorage());
        Logger.logDebug("FROM authlib:\n" + gson.toJson(vanillaLauncerProfiles));

        // save new data, including new access token, to disk
        try {
            wr = new BufferedWriter(new FileWriter("c:\\Users\\jikuja\\AppData\\Roaming\\.minecraft\\launcher_profiles.json"));
            wr.write(gson.toJson(vanillaLauncerProfiles));
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resp;

    }

    private static YggdrasilUserAuthentication callAuthLib (Map<String, Object> profile, UUID clientToken) {
        YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(Proxy.NO_PROXY, clientToken.toString())
                .createUserAuthentication(Agent.MINECRAFT);

        authentication.loadFromStorage(profile);
        if (authentication.canLogIn()) {
            try {
                authentication.logIn();
            } catch (Exception e) {
                Logger.logError("error", e);
                Logger.logError("error", e.getCause());
            }
        } else {
            Logger.logError("canLogIn() failed");
        }

        // TODO: add more test
        // e.g. authentication.canPlayOnline()

        return authentication;
    }
}
