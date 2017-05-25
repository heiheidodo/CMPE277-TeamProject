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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/*
add by email
friends list -- click see profile -- profile posts
public list -- click see profile -- profile posts
button click into this pending list


 */
public class FriendFragment extends Fragment implements  AsyncResponse{

    Button btnPending, btnSearch;
    EditText etEmail;
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
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        btnPending = (Button) view.findViewById(R.id.btnPending);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);
        btnPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PendingActivity.class));
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnWorker connWorker = new ConnWorker();
                connWorker.delegate = getAsyncResponse();
                connWorker.execute("addFriendRequest", "/user/"+UserInfo.getInstance().getEmail()+"/request/"+etEmail.getText().toString(), "PUT");
            }
        });
        lvPublicProfile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("position: "+position+" clicked");
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("name", ((TextView) view.findViewById(R.id.tvName)).getText().toString());
                intent.putExtra("email", ((TextView) view.findViewById(R.id.tvEmail)).getText().toString());
                startActivity(intent);

            }
        });
        ConnWorker connWorker = new ConnWorker();
        connWorker.delegate = getAsyncResponse();
        connWorker.execute("publicUsers", "/users/" + UserInfo.getInstance().getEmail(), "GET");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void getResponse(String str) {
//        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
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
