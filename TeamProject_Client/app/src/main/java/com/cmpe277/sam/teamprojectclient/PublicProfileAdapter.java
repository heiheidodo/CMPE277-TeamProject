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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sam on 5/20/17.
 */

public class PublicProfileAdapter extends ArrayAdapter{

    LayoutInflater li;
    ArrayList<PublicProfileModel> publicProfileList;
    int resource;
    PublicProfileAdapter.ViewHolder holder;

    public PublicProfileAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList objects) {
        super(context, resource, objects);
        li = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        publicProfileList = objects;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if(v == null){
            holder = new PublicProfileAdapter.ViewHolder();
            v = li.inflate(resource, null);
            holder.imageView = (ImageView) v.findViewById(R.id.ivImage);
            holder.tvName = (TextView) v.findViewById(R.id.tvName);
            holder.btnAdd = (Button) v.findViewById(R.id.btnAdd);
            holder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            v.setTag(holder);
        }else{
            holder = (PublicProfileAdapter.ViewHolder) v.getTag();
        }

        holder.tvName.setText(publicProfileList.get(position).getScreenName());
        if(publicProfileList.get(position).getSelfPic() != null){
            holder.imageView.setImageBitmap(publicProfileList.get(position).getSelfPic());
        }
        return v;
    }

    static class ViewHolder{
        public ImageView imageView;
        public TextView tvName;
        public Button btnAdd;
    }
}
