package com.jsi.mbrana.Helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * Created by surafel on 2/22/2017.
 */
public class NetworkTypeHelper {
    /**
     * Get the network info
     *
     * @param context
     * @return
     */
    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        NetworkInfo info = NetworkTypeHelper.getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    /**
     * Check if there is any connectivity to a Wifi network
     *
     * @param context
     * @return
     */
    public static boolean isConnectedWifi(Context context) {
        NetworkInfo info = NetworkTypeHelper.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Check if there is any connectivity to a mobile network
     *
     * @param context
     * @return
     */
    public static boolean isConnectedMobile(Context context) {
        NetworkInfo info = NetworkTypeHelper.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * Check if the connection is fast
     *
     * @param type
     * @param subType
     * @return
     */
    public static String ConnectionToNetworkType(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return "TYPE_WIFI";
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return "NETWORK_TYPE_1xRTT"; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return "NETWORK_TYPE_CDMA"; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return "NETWORK_TYPE_EDGE"; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return "NETWORK_TYPE_EVDO_0"; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return "NETWORK_TYPE_EVDO_A"; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return "NETWORK_TYPE_GPRS"; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return "NETWORK_TYPE_HSDPA"; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return "NETWORK_TYPE_HSPA"; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return "NETWORK_TYPE_HSUPA"; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return "NETWORK_TYPE_UMTS"; // ~ 400-7000 kbps
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return "NETWORK_TYPE_EHRPD"; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return "NETWORK_TYPE_EVDO_B"; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13 -- 3G
                    return "NETWORK_TYPE_HSPAP"; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8 -- 2G
                    return "NETWORK_TYPE_IDEN"; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11 -- 4G
                    return "NETWORK_TYPE_LTE"; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return "NETWORK_TYPE_UNKNOWN";
            }
        } else {
            return "NOT_MOBILE_OR_WIFI";
        }
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static InputStream getInputStream(HttpURLConnection connection)
            throws IOException {
        if (connection.getRequestMethod().equals("HEAD"))
            return null;
        String encoding = connection.getContentEncoding();
        if (encoding == null)
            return connection.getInputStream();
        else if (encoding.equalsIgnoreCase("gzip"))
            return new GZIPInputStream(connection.getInputStream());
        else if (encoding.equalsIgnoreCase("deflate"))
            return new InflaterInputStream(connection.getInputStream(),
                    new Inflater(true));
        return null;
    }

    public static InputStream getErrorStream(HttpURLConnection connection)
            throws IOException {
        if (connection.getRequestMethod().equals("HEAD"))
            return null;
        String encoding = connection.getContentEncoding();
        if (encoding == null)
            return connection.getErrorStream();
        else if (encoding.equalsIgnoreCase("gzip"))
            return new GZIPInputStream(connection.getErrorStream());
        else if (encoding.equalsIgnoreCase("deflate"))
            return new InflaterInputStream(connection.getErrorStream(),
                    new Inflater(true));

        return null;
    }
}
