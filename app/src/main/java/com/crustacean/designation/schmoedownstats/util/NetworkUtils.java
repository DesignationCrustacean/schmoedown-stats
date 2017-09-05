package com.crustacean.designation.schmoedownstats.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Humz on 06/04/2017.
 */

public class NetworkUtils
{
    private static final String BASE_URL = "https://dl.dropbox.com/s/";
    private static final String DB_URL = BASE_URL + "vto15u4s8bz0gcq/schmoedb.json?dl=0";
    private static final String DEV_DB_URL = "";
    private static final String VERSION_URL = BASE_URL + "g7og3f43ah6g6go/dbVersion.txt?dl=0";
    private static final String DEV_VERSION_URL = "";

    //private static final String RELEASE_DATE_FROM = "primary_release_date.gte";
    //private static final String RELEASE_DATE_TO = "primary_release_date.lte";


    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static String getRemoteSchmoeDb() throws IOException
    {
        return getRemoteFileContents(new URL(DB_URL));
    }

    public static String getRemoteSchmoeDbVersion() throws IOException
    {
        return getRemoteFileContents(new URL(VERSION_URL));
    }

    private static String getRemoteFileContents(URL url) throws IOException
    {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try
        {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput)
            {
                return scanner.next();
            }
            else
            {
                return null;
            }
        }
        finally
        {
            urlConnection.disconnect();
        }
    }
}
