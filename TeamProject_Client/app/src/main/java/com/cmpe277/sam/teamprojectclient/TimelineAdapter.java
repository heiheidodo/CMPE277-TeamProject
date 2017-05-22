package com.cmpe277.sam.teamprojectclient;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sam on 5/19/17.
 */

public class TimelineAdapter extends ArrayAdapter {

    LayoutInflater li;
    ArrayList<TimeLineModel> timelineList;
    int resource;
    ViewHolder holder;

    public TimelineAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList objects) {
        super(context, resource, objects);
        li = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        timelineList = objects;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;
        if(v == null){
            holder = new ViewHolder();
            v = li.inflate(resource, null);
            holder.imageView = (ImageView) v.findViewById(R.id.ivImage);
            holder.tvName = (TextView) v.findViewById(R.id.tvName);
            holder.tvEmail = (TextView) v.findViewById(R.id.tvEmail);
            holder.tvContent = (TextView) v.findViewById(R.id.tvContent);
            v.setTag(holder);
        }else{
            holder = (ViewHolder) v.getTag();
        }

        holder.tvName.setText(timelineList.get(position).getScreenName());
        holder.tvEmail.setText(timelineList.get(position).getEmail());
        holder.tvContent.setText(timelineList.get(position).getText());
        if(timelineList.get(position).getPic() != null){
            holder.imageView.setImageBitmap(timelineList.get(position).getPic());
        }


        return v;
    }

    static class ViewHolder{
        public ImageView imageView;
        public TextView tvName;
        public TextView tvEmail;
        public TextView tvContent;
    }
}
