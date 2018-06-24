package com.fr81.findtherightone;

import android.content.Intent;
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


/**
 * Fragment of the profile display in swipe
 */
public class FragmentProfile extends Fragment implements View.OnClickListener {

    private TextView tvName;
    private TextView tvAdjs;
    private TextView tvDescription;
    private ImageView ivStudentPic;

    private String name;
    private String adjs;
    private String description;
    private String mail;

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
        mail = getArguments().getString("mail");
        String picPath = getArguments().getString("pic");
        picPath = picPath.replace("\\", "/");
        picPath = picPath.replace("..", "");
        Log.i("pic", picPath);

        tvName.setText(name);
        tvAdjs.setText(adjs);
        tvDescription.setText(description);
        Picasso.get().load("http://skipti.fr" + picPath).noFade().into(ivStudentPic);
        ToolBox.blackAndWhitePic(ivStudentPic);
        ivStudentPic.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivStudentPic:
                Intent signup = new Intent(getActivity(), ProfileOtherUser.class);
                signup.putExtra("mail", mail);
                startActivity(signup);
                break;
        }
    }
}
