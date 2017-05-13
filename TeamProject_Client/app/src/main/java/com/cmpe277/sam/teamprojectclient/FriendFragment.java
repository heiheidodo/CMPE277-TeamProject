package com.cmpe277.sam.teamprojectclient;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/*
add by email
friends list -- click see profile -- profile posts
public list -- click see profile -- profile posts
button click into this pending list


 */
public class FriendFragment extends Fragment {

    Button btnPending;

    public FriendFragment() {
    }

    public static FriendFragment newInstance(String text) {
        FriendFragment friendFragment = new FriendFragment();
        return friendFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        btnPending = (Button) view.findViewById(R.id.btnPending);
        btnPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PendingActivity.class));
            }
        });
        return view;
    }
}
