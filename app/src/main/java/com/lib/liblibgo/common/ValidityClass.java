package com.lib.liblibgo.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.text.TextUtils;

public class ValidityClass {

    public static String EmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static String MobilePattern = "[0-9]{10}";

    public static boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && target.matches(EmailPattern));
    }

    public static boolean isValidMobile(String target) {
        return (!TextUtils.isEmpty(target) && target.matches(MobilePattern));
    }
    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
