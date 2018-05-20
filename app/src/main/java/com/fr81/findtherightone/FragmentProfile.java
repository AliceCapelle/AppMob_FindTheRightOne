package com.fr81.findtherightone;

import android.app.Notification;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;


public class FragmentProfile extends Fragment{

    private TextView tvName;
    private TextView tvAdjs;
    private TextView tvDescription;
    private ImageView ivStudentPic;

    private String name;
    private String adjs;
    private String description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_swipe_profile, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tvName = getView().findViewById(R.id.tvNameSwipe);
        tvAdjs = getView().findViewById(R.id.tvAdjSwipe);
        tvDescription = getView().findViewById(R.id.tvDescriptionSwipe);
        ivStudentPic = getView().findViewById(R.id.ivStudentPic);

        name = getArguments().getString("name");
        adjs = getArguments().getString("adjs");
        description = getArguments().getString("description");
        String picPath = getArguments().getString("pic");
        picPath = picPath.replace("\\", "/");
        picPath = picPath.replace("..", "");
        Log.i("pic", picPath);

        tvName.setText(name);
        tvAdjs.setText(adjs);
        tvDescription.setText(description);
        Picasso.get().load("http://tinder.student.elwinar.com" + picPath).noFade().into(ivStudentPic);
        ToolBox.blackAndWhitePic(ivStudentPic);


    }


}
