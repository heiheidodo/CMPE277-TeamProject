package com.cmpe277.sam.teamprojectclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


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

    private static final String STARTING_TEXT = "Four Buttons Bottom Navigation";

    public TimeLineFragment() {
    }

    public static TimeLineFragment newInstance(String text) {
        Bundle args = new Bundle();
        args.putString(STARTING_TEXT, text);

        TimeLineFragment timeLineFragment = new TimeLineFragment();
        timeLineFragment.setArguments(args);
        return timeLineFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("oncreateview", "create list view");
        View view = inflater.inflate(R.layout.fragment_time_line, container, false);
        ConnWorker connWorker = new ConnWorker();
        connWorker.execute("timeline", "/post/"+UserInfo.getInstance().getEmail()+"/timeline", "GET");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d("onactivitycreate", "create list view");
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.timeline_test, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void getResponse(String str) {

    }
}
