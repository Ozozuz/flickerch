package com.example.flickrapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SavedImagesAdapter extends RecyclerView.Adapter<FlickerImageViewHolder> {

    Context mContext;
    List<Photo> mPhotos;

    public SavedImagesAdapter(Context ct, List<Photo> imgs){
        this.mContext = ct;
        this.mPhotos = imgs;
    }

    @NonNull
    @Override
    public FlickerImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.browse, parent, false);
        return new FlickerImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlickerImageViewHolder holder, int position) {
        if(mPhotos==null || mPhotos.size()==0){
            holder.thumbnail.setImageResource(R.drawable.ic_launcher_foreground);
            return;
        }
        Photo photoItem = mPhotos.get(position);
        Picasso.with(mContext).load(photoItem.getUrl())
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        if (this.mPhotos != null && mPhotos.size()>0)
            return this.mPhotos.size();
        else
            return 0;
    }

    public void loadNewData(Photo pic){
        this.mPhotos.add(pic);
        notifyDataSetChanged();
    }

    public void delete(int position){
        this.mPhotos.remove(position);
        notifyDataSetChanged();
    }

    public Photo getPhoto(int p){
        if(mPhotos!=null && mPhotos.size()>=p )
            return mPhotos.get(p);
        else return null;
    }
}
