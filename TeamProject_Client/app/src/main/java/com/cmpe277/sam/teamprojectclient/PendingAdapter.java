package com.cmpe277.sam.teamprojectclient;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sam on 5/22/17.
 */

public class PendingAdapter extends ArrayAdapter {

    LayoutInflater li;
    ArrayList<String> pendingList;
    int resource;
    ViewHolder holder;
    Context context;
    CallbackToPendingAct callback;


    public PendingAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList objects) {
        super(context, resource, objects);
        li = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        pendingList = objects;
        this.resource = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if(v == null){
            holder = new ViewHolder();
            v = li.inflate(resource, null);
            holder.tvEmail = (TextView) v.findViewById(R.id.tvEmail);
            holder.btnAccept = (Button) v.findViewById(R.id.btnAccept);
            holder.btnDeny = (Button) v.findViewById(R.id.btnDeny);
            holder.btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConnWorker connWorker = new ConnWorker();
                    connWorker.delegate = callback.getResponse();
                    connWorker.execute("addFriendRequest", "/user/"+UserInfo.getInstance().getEmail()+"/accept/"+holder.tvEmail.getText().toString(), "PUT");
                }
            });
            holder.btnDeny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConnWorker connWorker = new ConnWorker();
                    connWorker.delegate = callback.getResponse();
                    connWorker.execute("addFriendRequest", "/user/"+UserInfo.getInstance().getEmail()+"/deny/"+holder.tvEmail.getText().toString(), "PUT");
                }
            });
            v.setTag(holder);
        }else{
            holder = (ViewHolder) v.getTag();
        }

        holder.tvEmail.setText(pendingList.get(position));
        return v;
    }

    static class ViewHolder{
        public TextView tvEmail;
        public Button btnAccept;
        public Button btnDeny;
    }

    public interface CallbackToPendingAct{
        public AsyncResponse getResponse();
    }

    public void setCallback(CallbackToPendingAct callback){
        this.callback = callback;
    }

}
