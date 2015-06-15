package ar.gob.buenosaires.camino.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

import ar.gob.buenosaires.camino.BuildConfig;
import ar.gob.buenosaires.camino.CaminoApplication;

public class Utils {

    public static final String SHARED_PREFERENCES_GENERAL_APP = "app_preferences";

    public static boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) CaminoApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((px / displayMetrics.density) + 0.5);
    }


    public static boolean isBlank(String text) {
        return isEmpty(text != null ? text.trim() : text);
    }

    public static boolean isNotBlank(String text) {
        return !isBlank(text);
    }

    public static boolean isEmpty(String text) {
        return text == null || text.length() == 0;
    }

    public static boolean isNotEmpty(String text) {
        return !isEmpty(text);
    }

    public static final boolean isDebug() {
        /*
         * Try to prevent ADT bug:
		 * If BuildConfig.DEBUG is set to false, then assume it's OK,
		 * else use our flag.
		 */
        return BuildConfig.DEBUG && false;
    }
};
