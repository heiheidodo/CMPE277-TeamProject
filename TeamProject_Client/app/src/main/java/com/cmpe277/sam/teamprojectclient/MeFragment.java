package com.cmpe277.sam.teamprojectclient;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;


public class MeFragment extends Fragment implements AsyncResponse{

    private ImageButton portrait_image_button;
    private Context mContext;
    private TextView meName;
    private TextView meAboutMe;
    private ImageButton me_message_button;
    private ImageButton me_myPosts_button;
    private ImageButton me_setting_button;
    Button btnLogout;

    public MeFragment() {
    }

    public static MeFragment newInstance(String text) {

        System.out.println("create fragement instance asdqwd12d");
        MeFragment meFragment = new MeFragment();

        return meFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ConnWorker connWorker = new ConnWorker();
        connWorker.delegate = getResponse();
        connWorker.execute("getMe", "/user", "GET");
        SystemClock.sleep(500);
        System.out.println("create fragement me layout from asdasdsdasd");
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        mContext = getContext();
        UserInfo user = UserInfo.getInstance();

        portrait_image_button = (ImageButton)view.findViewById(R.id.me_portrait);
        portrait_image_button.setImageBitmap(ProfileSettingActivity.getImage());
        portrait_image_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProfileSettingActivity.class);
                startActivity(intent);
            }
        });
        me_setting_button = (ImageButton)view.findViewById(R.id.me_setting_button);
        me_setting_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProfileSettingActivity.class);
                startActivity(intent);
            }
        });

        meName = (TextView)view.findViewById(R.id.me_name);
        meName.setText(user.getScreenName());
        meAboutMe = (TextView)view.findViewById(R.id.me_aboutMe);
        meAboutMe.setText(user.getAboutMe());

        me_myPosts_button = (ImageButton)view.findViewById(R.id.me_myPosts_button);
        me_myPosts_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //To my posts!!!!!!!!!!!!
               startActivity(new Intent(getContext(), MyPostActivity.class));
            }
        });

        me_message_button = (ImageButton)view.findViewById(R.id.me_message_button);
        me_message_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //To my Message!!!!!!!!!!!!
            }
        });

        btnLogout = (Button) view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SignUpActivity.class));

            }
        });

        return view;
    }

    @Override
    public void getResponse(String str) {

    }

    @Override
    public void getJSONResponse(ArrayList array) {

    }

    public AsyncResponse getResponse() {
        return this;
    }
}
