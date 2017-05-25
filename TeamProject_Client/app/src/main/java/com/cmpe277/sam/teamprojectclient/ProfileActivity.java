package com.cmpe277.sam.teamprojectclient;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements AsyncResponse{

    TextView tvName, tvEmail;
    ListView lvPosts;
    TimelineAdapter postsAdapter;
    ArrayList<TimeLineModel> posts;
    Button btnFollow, btnAdd, btnMessage;
    Activity self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        self = this;
        ConnWorker connWorker = new ConnWorker();
        connWorker.delegate = getAsyncResponse();
        connWorker.execute("getPublicPosts", "/user/"+getIntent().getStringExtra("email")+"/from/"+UserInfo.getInstance().getEmail(), "GET");

        tvName = (TextView) findViewById(R.id.tvName);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvName.setText(getIntent().getStringExtra("name"));
        tvEmail.setText(getIntent().getStringExtra("email"));
        lvPosts = (ListView) findViewById(R.id.lvPosts);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnFollow = (Button) findViewById(R.id.btnFollow);
        btnMessage = (Button) findViewById(R.id.btnMessage);

        posts = new ArrayList<>();
        TimeLineModel testModel = new TimeLineModel();
        testModel.setScreenName("sam");
        testModel.setText("test test");
        posts.add(testModel);
        postsAdapter = new TimelineAdapter(getApplication(), R.layout.timeline_item, posts);
        lvPosts.setAdapter(postsAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnWorker connWorker = new ConnWorker();
                connWorker.delegate = getAsyncResponse();
                connWorker.execute("addFriendRequest", "/user/"+UserInfo.getInstance().getEmail()+"/request/"+tvEmail.getText().toString(), "PUT");

            }
        });

        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnWorker connWorker = new ConnWorker();
                connWorker.delegate = getAsyncResponse();
                connWorker.execute("addFriendRequest", "/user/"+UserInfo.getInstance().getEmail()+"/follow/"+tvEmail.getText().toString(), "PUT");
            }
        });

        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDialog messageDialog = new MessageDialog(self, tvEmail.getText().toString(), tvName.getText().toString());
                messageDialog.show();
            }
        });

    }


    @Override
    public void getResponse(String str) {
//        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        //add follow
        if(str == null) return;
        System.out.println("can add or follow? "+str);
        if(str.equals("falsetrue")){
            btnAdd.setVisibility(View.GONE);
        }else if(str.equals("truefalse")){
            btnFollow.setVisibility(View.GONE);
        }else if(str.equals("falsefalse")){
            btnAdd.setVisibility(View.GONE);
            btnFollow.setVisibility(View.GONE);
        }

    }

    @Override
    public void getJSONResponse(ArrayList array) {

        if(array != null){
            System.out.println("get the posts arr from server");
            postsAdapter.clear();
            postsAdapter.addAll(array);
            postsAdapter.notifyDataSetChanged();
        }
    }

    public AsyncResponse getAsyncResponse(){
        return this;
    }
}
