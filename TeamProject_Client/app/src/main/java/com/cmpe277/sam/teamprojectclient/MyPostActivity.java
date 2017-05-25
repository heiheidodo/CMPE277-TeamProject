package com.cmpe277.sam.teamprojectclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by sam on 5/24/17.
 */

public class MyPostActivity extends AppCompatActivity implements AsyncResponse, PendingAdapter.CallbackToPendingAct{

    ListView lvPost;
    TimelineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypost);
        lvPost = (ListView) findViewById(R.id.lvMyPost);
        ArrayList arrTest = new ArrayList();
        TimeLineModel testModel = new TimeLineModel();
        testModel.setEmail("test");
        testModel.setScreenName("test");
        testModel.setText("test");
        arrTest.add(testModel);
        adapter = new TimelineAdapter(getApplication(), R.layout.timeline_item, arrTest);
        lvPost.setAdapter(adapter);
        ConnWorker connWorker = new ConnWorker();
        connWorker.delegate = this;
        connWorker.execute("getMyPosts", "/user/"+UserInfo.getInstance().getEmail(), "GET");
    }

    @Override
    public void getResponse(String str) {
        System.out.println(str);
        if(str == "get pending list fail"){
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getJSONResponse(ArrayList array) {
        System.out.println("get the my post arr from server");
        if(!array.isEmpty()){
            adapter.clear();
            adapter.addAll(array);
            adapter.notifyDataSetChanged();
        }else{
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public AsyncResponse getResponse() {
        return this;
    }
}
