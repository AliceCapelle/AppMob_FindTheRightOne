package com.fr81.findtherightone;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

public class Profile_Other_User extends AppCompatActivity {
    private BackendConnection b = new BackendConnection();
    private TextView tvDescription_Other;
    private TextView tv_Other_Name;
    private TextView tvAdjectives_Other;
    private TextView tvMatch_Other;
    private ImageView imgProfile_Other;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__other__user);
        String otherstudent = loadData("getthemailafter the click on swipe page");
        JSONObject info_other_student = decodeData(otherstudent);
        try {
            tv_Other_Name = findViewById(R.id.tv_Other_Name);
            tv_Other_Name.setText(info_other_student.getString("surname"));
            tvAdjectives_Other = findViewById(R.id.tvAdjectives_Other);
            tvAdjectives_Other.setText(info_other_student.getString("adj1") + " - " +
                    info_other_student.getString("adj2") + " - " +
                    info_other_student.getString("adj3"));
            tvDescription_Other = findViewById(R.id.tvDescription_Other);
            tvDescription_Other.setText(info_other_student.getString("description"));
            tvMatch_Other = findViewById(R.id.tvMatch_Other);
            tvMatch_Other.setText(info_other_student.getString("match"));

            String picPath = info_other_student.getString("pic");
            picPath = picPath.replace("\\", "/");
            picPath = picPath.replace("..", "");
            Log.i("PICTURE", "http://tinder.student.elwinar.com" + picPath);

            Picasso.get().load("http://tinder.student.elwinar.com" + picPath).noFade().into(imgProfile_Other);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected String loadData(String mail) {
        HttpURLConnection conn = null;
        String result = null;
        try{
            conn  = b.connect("https://tinder.student.elwinar.com/controller/updateprofile.php", "GET");
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("mail", mail);
            String query = builder.build().getEncodedQuery();
            b.sendData(conn, query);
            result = b.getData(conn);

        }catch(IOException e){
            e.printStackTrace();
        }
        return result;
    }
    protected JSONObject  decodeData(String result){
        try {
            JSONObject json = new JSONObject(result.toString());
            JSONObject student = json.getJSONObject("student");
            return student;

        }catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}


