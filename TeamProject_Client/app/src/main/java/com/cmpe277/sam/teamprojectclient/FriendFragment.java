package com.cmpe277.sam.teamprojectclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/*
add by email
friends list -- click see profile -- profile posts
public list -- click see profile -- profile posts
button click into this pending list


 */
public class FriendFragment extends Fragment implements AdapterView.OnItemClickListener, AsyncResponse{

    Button btnPending;
    ListView lvPublicProfile;
    PublicProfileAdapter publicProfileAdapter;
    ArrayList<PublicProfileModel> itemList;

    public FriendFragment() {
    }

    public static FriendFragment newInstance(String text) {
        FriendFragment friendFragment = new FriendFragment();
        return friendFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        itemList = new ArrayList<>();
        PublicProfileModel testModel = new PublicProfileModel();
        testModel.setScreenName("sam");
        itemList.add(testModel);
        lvPublicProfile = (ListView) view.findViewById(R.id.publicList);
        publicProfileAdapter = new PublicProfileAdapter(getContext(), R.layout.publicprofile_item, itemList);
        lvPublicProfile.setAdapter(publicProfileAdapter);
        btnPending = (Button) view.findViewById(R.id.btnPending);
        btnPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PendingActivity.class));
            }
        });
        ConnWorker connWorker = new ConnWorker();
        connWorker.delegate = getAsyncResponse();
        connWorker.execute("publicUsers", "/users", "GET");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void getResponse(String str) {

    }

    @Override
    public void getJSONResponse(ArrayList array) {
        if(array != null){
            System.out.println("get the public profile arr from server");
            publicProfileAdapter.clear();
            publicProfileAdapter.addAll(array);
            publicProfileAdapter.notifyDataSetChanged();
        }
    }

    public AsyncResponse getAsyncResponse(){
        return this;
    }
}
