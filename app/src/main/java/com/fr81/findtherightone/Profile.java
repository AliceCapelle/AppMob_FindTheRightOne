package com.fr81.findtherightone;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Class for profile activity
 */
public class Profile extends AppCompatActivity implements View.OnClickListener {

    private BackendConnection b = new BackendConnection();
    private TextView tvDescription;
    private TextView tvName;
    private TextView tvAdjectives;
    private TextView tvMatch;
    private ImageView imgProfile;
    private Button bSwipe;
    private Button bEdit;
    private SharedPreferences sharedPreferences;
    private ProgressBar p;
    private Button bCloseEdit;
    private boolean editMode = false;
    private Button bDecoP;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private byte[] imgArray;
    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        p = findViewById(R.id.pBProfile);
        p.setVisibility(View.VISIBLE);

        bSwipe = findViewById(R.id.bSwipeP);
        bEdit = findViewById(R.id.bEdit);
        bCloseEdit = findViewById(R.id.bCloseEdit);
        bDecoP = findViewById(R.id.bDecoP);

        bSwipe.setOnClickListener(this);
        bEdit.setOnClickListener(this);
        bCloseEdit.setOnClickListener(this);
        bDecoP.setOnClickListener(this);

        tvDescription = findViewById(R.id.tvDescription);
        tvName = findViewById(R.id.tvName);
        tvAdjectives = findViewById(R.id.tvAdjectives);
        tvMatch = findViewById(R.id.tvMatch);
        imgProfile = findViewById(R.id.imgProfile);

        sharedPreferences = getBaseContext().getSharedPreferences("PREFS", MODE_PRIVATE);
        String mail = sharedPreferences.getString("PREFS_MAIL", null);

        new Profile.AsyncProfil().execute(mail);

        ToolBox.blackAndWhitePic(imgProfile);


    }

    @Override
    public void onClick(View v) {
        EditText etDescription = findViewById(R.id.etDescription);
        TextView tvDescription = findViewById(R.id.tvDescription);
        switch (v.getId()) {
            case R.id.bDecoP:
                getSharedPreferences("PREFS", 0).edit().clear().commit();
                Intent home = new Intent(this, Home.class);
                startActivity(home);
                finish();
                break;
            case R.id.bSwipeP:
                Intent swipe = new Intent(this, Swipe.class);
                startActivity(swipe);
                break;
            case R.id.bEdit:
                //Toast.makeText(Profile.this, "Pas encore disponible", Toast.LENGTH_SHORT).show();
                editMode = !editMode;

                //If we want to edit the profile description
                if(editMode) {
                    etDescription.setVisibility(View.VISIBLE);
                    tvDescription.setVisibility(View.INVISIBLE);
                    bCloseEdit.setVisibility(View.VISIBLE);
                    imgProfile.setOnClickListener(this);
                }
                else {
                    //Validate
                    bCloseEdit.setVisibility(View.INVISIBLE);
                    String mail = sharedPreferences.getString("PREFS_MAIL", null);
                    new Profile.AsyncProfil().execute(mail, etDescription.getText().toString(), encodedImage);
                    etDescription.setVisibility(View.INVISIBLE);
                    tvDescription.setVisibility(View.VISIBLE);
                    imgProfile.setOnClickListener(null);
                }
                break;
            case R.id.imgProfile:
                openCamera();
                break;
            case R.id.bCloseEdit:
                etDescription.setVisibility(View.INVISIBLE);
                tvDescription.setVisibility(View.VISIBLE);
                bCloseEdit.setVisibility(View.INVISIBLE);
                editMode = !editMode;
                break;
        }
    }





        private class AsyncProfil extends AsyncTask<String, String, String> {

        /**
         * Call server with post request to retrieve all info needed for the connected student
         * @param params take mail of student
         * @return
         */
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn = null;
            String result = "fail";

            try {
                conn = b.connect("http://skipti.fr/controller/updateprofile.php", "POST");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("mail", params[0]);

                if(params.length > 1){
                    builder.appendQueryParameter("description", params[1]);
                    if(params[1] != null){
                        builder.appendQueryParameter("image", params[2]);
                    }
                }


                String query = builder.build().getEncodedQuery();
                b.sendData(conn, query);

                result = b.getData(conn);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServerException e) {
                Log.i("Profile other user", "Dev didn't do his job");
            }
            return result;
        }

        /**
         * Decode json and get element to feed UI
         * @param result string with all student info
         */
        @Override
        protected void onPostExecute(String result) {

            try {
                Log.i("profile", result);
                JSONObject json = new JSONObject(result.toString());
                JSONObject student = json.getJSONObject("student");
                Log.i("profile", json.toString());
                Log.i("profile", student.toString());

                String description = student.getString("description");
                tvDescription.setText(description.equals("null") ? "Pas de description" : description);
                tvName.setText(student.getString("surname"));
                String adjectives = student.getString("adj1") + " - " +
                                    student.getString("adj2") + " - " +
                                    student.getString("adj3");
                tvAdjectives.setText(adjectives);
                String match = json.getString("match") + " match(s)";
                tvMatch.setText(match);

                String picPath = student.getString("pic");
                picPath = picPath.replace("\\", "/");
                picPath = picPath.replace("..", "");

                Picasso.get().load("http://skipti.fr" + picPath).noFade().into(imgProfile, new Callback() {
                    @Override
                    public void onSuccess() {
                        p.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void openCamera(){
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }


        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imgProfile.setImageBitmap(photo);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                imgArray = baos.toByteArray();
                encodedImage = Base64.encodeToString(imgArray, Base64.DEFAULT);
            }
        }

}

