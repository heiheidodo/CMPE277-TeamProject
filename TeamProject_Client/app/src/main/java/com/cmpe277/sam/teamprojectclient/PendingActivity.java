package com.cmpe277.sam.teamprojectclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PendingActivity extends AppCompatActivity implements AsyncResponse{

    ListView lvPending;
    PendingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);
        lvPending = (ListView) findViewById(R.id.pendingList);
        adapter = new PendingAdapter(getApplication(), R.layout.pending_item, UserInfo.getInstance().getPendingList());
        lvPending.setAdapter(adapter);
    }

    @Override
    public void getResponse(String str) {

    }

    @Override
    public void getJSONResponse(ArrayList array) {

    }

    public AsyncResponse getAsyncResponse(){
        return this;
    }
}
