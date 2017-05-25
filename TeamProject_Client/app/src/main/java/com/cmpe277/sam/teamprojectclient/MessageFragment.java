package com.cmpe277.sam.teamprojectclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageFragment extends Fragment implements AsyncResponse{

    Button btnMessage;
    ListView lvMessage;
    MessageAdapter adapter;
    ArrayList<MessageModel> itemList;

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

        itemList = new ArrayList<>();
        MessageModel testModel = new MessageModel();
        testModel.setFromScreenName("sam");
        testModel.setToScreenName("mick");
        testModel.setMessage("content test");
        itemList.add(testModel);
        ConnWorker connWorker = new ConnWorker();
        connWorker.delegate = getAsyncResponse();
        connWorker.execute("getInMailList", "/inMail/"+UserInfo.getInstance().getEmail(), "GET");

        lvMessage = (ListView) view.findViewById(R.id.lvMsg);
        adapter = new MessageAdapter(getContext(), R.layout.message_item, itemList);
        lvMessage.setAdapter(adapter);

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

    public AsyncResponse getAsyncResponse(){
        return this;
    }

    @Override
    public void getResponse(String str) {

    }

    @Override
    public void getJSONResponse(ArrayList array) {
        if(array != null){
            System.out.println("get the message arr from server");
            adapter.clear();
            adapter.addAll(array);
            adapter.notifyDataSetChanged();
        }
    }
}
