package com.example.flickrapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SavedImages extends BaseActivity implements RecyclerItemClickListener.OnRecyclerClickListener {

    RecyclerView mRecyclerView;
    SavedImagesAdapter mAdapter;

    String mUID;
    FirebaseAuth mAuth;
    FirebaseFirestore mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_images);

        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseFirestore.getInstance();
        mUID = mAuth.getUid();

        activateToolbar(true);

        try {
            getSavedImages();
        } catch (JSONException e) {
            Log.w("savedimages", "error calling getSavedImages()");
        }


        mRecyclerView = findViewById(R.id.recycler_view123);
        mAdapter = new SavedImagesAdapter(this, new ArrayList<Photo>());

        RecyclerItemClickListener clickListener = new RecyclerItemClickListener(this, mRecyclerView, this);
        mRecyclerView.addOnItemTouchListener(clickListener);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this ));
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, PhotoDetailStoredActivity.class);
        intent.putExtra(PHOTO_TRANSFER, mAdapter.getPhoto(position));
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, final int position) {

        PopupMenu menu = new PopupMenu(this, view);
        menu.inflate(R.menu.popup_savedimages_menu);
        menu.setGravity(Gravity.RIGHT);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.delete_item:
                        Photo toDelete = mAdapter.getPhoto(position);
                        delete_from_database(toDelete);
                        mAdapter.delete(position);
                        return true;

                    default:
                        return false;
                }
            }
        });
        menu.show();


    }

    private void delete_from_database(Photo pic) {

        String pic_url = pic.getUrl();
        String filename = pic_url.substring(pic_url.lastIndexOf('/'));
        String link = "http://flickerch-backend.herokuapp.com/database/" + mUID + filename;

        Log.w("DELETING", filename);

        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, link, null, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("DELETE", "Error deliting file fromdatabase");
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(mJsonObjectRequest);

    }

    private void getSavedImages() throws JSONException {

        String link = "http://flickerch-backend.herokuapp.com/database/" + mUID;


        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, link, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray itemsArray = null;
                try {
                    itemsArray = response.getJSONArray("photos");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i=0; i < itemsArray.length(); i++){

                    String title = null;
                    String author = null;
                    String tags = null;
                    String url = null;

                    try {
                        JSONObject obj = itemsArray.getJSONObject(i);
                        title = obj.getString("title");
                        author = obj.getString("author");
                        tags = obj.getString("tags");
                        url = obj.getString("url");
                    } catch (JSONException e){

                    }

                    Photo toAdd = new Photo(title, author, url, tags);
                    mAdapter.loadNewData(toAdd);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("savedimages", error.getMessage());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(mJsonObjectRequest);



    }

}
