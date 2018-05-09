package com.fr81.findtherightone;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by acapelle on 09/05/2018.
 */

public class BackendConnection {

    public static HttpURLConnection connect(String link, String methode) throws IOException {
        final int CONNECTION_TIMEOUT = 10000;
        final int READ_TIMEOUT = 15000;
        HttpURLConnection conn = null;
        URL url = null;
        url = new URL(link);
        conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(READ_TIMEOUT);
        conn.setConnectTimeout(CONNECTION_TIMEOUT);
        conn.setRequestMethod(methode);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        Log.i("backendconnection", "connect: we're done");
        return conn;
    }
}
