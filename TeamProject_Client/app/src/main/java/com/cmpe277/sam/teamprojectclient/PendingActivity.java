package com.cmpe277.sam.teamprojectclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PendingActivity extends AppCompatActivity implements AsyncResponse, PendingAdapter.CallbackToPendingAct {

    ListView lvPending;
    PendingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);
        lvPending = (ListView) findViewById(R.id.pendingList);
        ArrayList arrTest = new ArrayList();
        arrTest.add("None");
        adapter = new PendingAdapter(getApplication(), R.layout.pending_item, arrTest);
        adapter.setCallback(this);
        lvPending.setAdapter(adapter);
        ConnWorker connWorker = new ConnWorker();
        connWorker.delegate = this;
        connWorker.execute("getPending", "/users/"+UserInfo.getInstance().getEmail()+"/pending", "GET");
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
        System.out.println("get the pending arr from server");
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
