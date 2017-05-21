package com.cmpe277.sam.teamprojectclient;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/*
*
* json array
* {text, screen name, pic}
*
* 1self
* 2friends
* 3following
 */


public class TimeLineFragment extends ListFragment implements AdapterView.OnItemClickListener, AsyncResponse{

    ListView lvTimeLine;
    TimelineAdapter timelineAdapter;
    ArrayList<TimeLineModel> itemList;

    public TimeLineFragment() {
    }

    public static TimeLineFragment newInstance(String text) {

        TimeLineFragment timeLineFragment = new TimeLineFragment();
        return timeLineFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("oncreateview", "create list view");
        View view = inflater.inflate(R.layout.fragment_time_line, container, false);
        itemList = new ArrayList<>();
        TimeLineModel testModel = new TimeLineModel();
        testModel.setScreenName("sam");
        testModel.setText("test test");
        itemList.add(testModel);
        ConnWorker connWorker = new ConnWorker();
        connWorker.delegate = getAsyncResponse();
        connWorker.execute("timeline", "/post/"+UserInfo.getInstance().getEmail()+"/timeline", "GET");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d("onactivitycreate", "create list view");
        super.onActivityCreated(savedInstanceState);
//        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.timeline_test, android.R.layout.simple_list_item_1);
//        setListAdapter(adapter);
        lvTimeLine = getListView();
        timelineAdapter = new TimelineAdapter(getContext(), R.layout.timeline_item, itemList);
        lvTimeLine.setAdapter(timelineAdapter);
        getListView().setOnItemClickListener(this);
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
            System.out.println("get the timeline arr from server");
            timelineAdapter.clear();
            timelineAdapter.addAll(array);
            timelineAdapter.notifyDataSetChanged();
        }
    }

    public AsyncResponse getAsyncResponse(){
        return this;
    }
}
