package com.cmpe277.sam.teamprojectclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MessageFragment extends Fragment {

    Button btnMessage;

    public MessageFragment() {

    }

    public static MessageFragment newInstance(String text) {

        MessageFragment messageFragment = new MessageFragment();
        return messageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        btnMessage = (Button) view.findViewById(R.id.btnMessage);
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDialog messageDialog = new MessageDialog(getActivity());
                messageDialog.show();
            }
        });
        return view;
    }
}
