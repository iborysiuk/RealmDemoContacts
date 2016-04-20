package com.realmcontacts.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

import java.io.IOException;

/**
 * Created by Yuriy on 2016-04-18 RealmContacts.
 */
public class NetworkUtils {

    public static boolean isNetworkConnected(@NonNull Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null;
    }

    public static boolean isOnline(@NonNull Context context) {
        if (!isNetworkConnected(context)) return false;
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }


}
