package com.example.flickrapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class FlickerImageViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "FlickerImageViewHolder";
    ImageView thumbnail =null;

    public FlickerImageViewHolder(@NonNull View itemView) {
        super(itemView);
        this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
    }
}

