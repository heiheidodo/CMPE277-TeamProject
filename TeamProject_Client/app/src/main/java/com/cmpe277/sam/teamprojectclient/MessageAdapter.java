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
 * Created by sam on 5/24/17.
 */

public class MessageAdapter extends ArrayAdapter {

    LayoutInflater li;
    ArrayList<MessageModel> messagelist;
    int resource;
    MessageAdapter.ViewHolder holder;
    CallbackToMsgFragment callback;

    public MessageAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList objects) {
        super(context, resource, objects);
        li = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        messagelist = objects;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;
        if(v == null){
            holder = new MessageAdapter.ViewHolder();
            v = li.inflate(resource, null);
            holder.tvFromScreenName = (TextView) v.findViewById(R.id.tvFromEmail);
            holder.tvToScreenName = (TextView) v.findViewById(R.id.tvToEmail);
            holder.tvContent = (TextView) v.findViewById(R.id.tvContent);
            holder.tvTime = (TextView) v.findViewById(R.id.tvTime);
            holder.btnDelete = (Button) v.findViewById(R.id.btnDelete);
            v.setTag(holder);
        }else{
            holder = (MessageAdapter.ViewHolder) v.getTag();
        }

        holder.tvFromScreenName.setText(messagelist.get(position).getFromScreenName());
        holder.tvToScreenName.setText(messagelist.get(position).getToScreenName());
        holder.tvContent.setText(messagelist.get(position).getMessage());
        holder.tvTime = (TextView) v.findViewById(R.id.tvTime);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnWorker connWorker = new ConnWorker();
                connWorker.delegate = callback.getResponse();
                connWorker.execute("deleteMail", "/inMail/"+messagelist.get(position).getId(), "DELETE");
            }
        });

        return v;
    }

    static class ViewHolder{
        public TextView tvFromScreenName;
        public TextView tvToScreenName;
        public TextView tvContent;
        public TextView tvTime;
        public Button btnDelete;
    }

    public interface CallbackToMsgFragment{
        public AsyncResponse getResponse();
    }

    public void setCallback(MessageAdapter.CallbackToMsgFragment callback){
        this.callback = callback;
    }
}
