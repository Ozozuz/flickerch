package com.example.flickrapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

class FlickerRecyclerViewAdapter extends RecyclerView.Adapter<FlickerImageViewHolder> {
    private static final String TAG = "FlickerRecyclerViewAdap";
    private List<Photo> photoList;
    private Context context;

    public FlickerRecyclerViewAdapter(Context context, List<Photo> photoList) {
        this.photoList = photoList;
        this.context = context;
    }

    @NonNull
    @Override
    public FlickerImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Called by the layout_manager (android stuff) when a new view is need.
        //Log.d(TAG, "onCreateViewHolder: New view requested");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent, false);
        return new FlickerImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlickerImageViewHolder holder, int position) {
        //called by the layoutManager when it wants new data to be stored in an EXISTING row, holder.
        //Log.d(TAG, "onBindViewHolder: Need data to be stored");
        if(photoList==null || photoList.size()==0){
            holder.thumbnail.setImageResource(R.drawable.ic_launcher_foreground);
            return;
        }
        Photo photoItem = photoList.get(position);
        Picasso.with(context).load(photoItem.getUrl())
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        if (this.photoList != null && photoList.size()>0)
            return this.photoList.size();
        else
            return 0;
    }

    void loadNewData(List<Photo> photos){
        this.photoList=photos;
        notifyDataSetChanged();
    }

    public Photo getPhoto(int p){
        if(photoList!=null && photoList.size()>=p )
            return photoList.get(p);
        else return null;
    }
}
