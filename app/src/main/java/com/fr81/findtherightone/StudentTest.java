package com.fr81.findtherightone;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import net.alhazmy13.wordcloud.ColorTemplate;
import net.alhazmy13.wordcloud.WordCloud;
import net.alhazmy13.wordcloud.WordCloudView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Random;


public class StudentTest extends AppCompatActivity {
    BackendConnection b;
    WordCloudView wordCloud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        wordCloud = findViewById(R.id.wordCloud);
        new StudentTest.AsyncGetAdj().execute();

    }

    private class AsyncGetAdj extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn = null;
            String result = "fail";

            try {
                conn = b.connect("http://skipti.fr/controller/get_adjs.php", "POST");



                result = b.getData(conn);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServerException e) {
                Log.i("Test", "Dev didn't do his job");
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("test", result);
            try {
                JSONArray jsonArray = new JSONArray(result);
                Log.i("test", jsonArray.toString());
                ArrayList<String> list = new ArrayList<String>();
                if (jsonArray != null) {
                    int len = jsonArray.length();
                    for (int i=0;i<len;i++){
                        list.add(jsonArray.get(i).toString());
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
}
}

