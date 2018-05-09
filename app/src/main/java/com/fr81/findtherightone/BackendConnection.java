package com.fr81.findtherightone;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by acapelle on 09/05/2018.
 * Simple class to help make request to server
 */

public class BackendConnection {

    /**
     * Initialize request to server
     *
     * @param link    URL of server page we call
     * @param methode GET or POST
     * @return HttpURLConnection
     * @throws IOException
     */
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
        return conn;
    }

    /**
     * Send parameters to server (only for POST)
     * @param conn HttpURLConnection initialize with connect()
     * @param query
     * @throws IOException
     */
    public static void sendData(HttpURLConnection conn, String query) throws IOException {
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(query);
        writer.flush();
        writer.close();
        os.close();
        conn.connect();
    }

    /**
     * Retrieve data from server
     * @param conn HttpURLConnection initialize with connect()
     * @return String echo by server
     * @throws IOException
     */
    public static String getData(HttpURLConnection conn) throws IOException {
        int response_code = 0;
        response_code = conn.getResponseCode();
        StringBuilder result = null;
        if (response_code == HttpURLConnection.HTTP_OK) {
            InputStream input = null;
            input = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        }
        return result.toString();
    }
}
