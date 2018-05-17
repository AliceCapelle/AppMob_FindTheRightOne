package com.fr81.findtherightone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


public class FragmentProfile extends Fragment{

    private TextView tvName;
    private TextView tvAdjs;
    private TextView tvDescription;

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

        name = getArguments().getString("name");
        adjs = getArguments().getString("adjs");
        description = getArguments().getString("description");

        tvName.setText(name);
        tvAdjs.setText(adjs);
        tvDescription.setText(description);
    }


}
