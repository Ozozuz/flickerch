package com.example.flickrapp;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class PhotoDetailActivity extends BaseActivity {
    FloatingActionButton fab;
    TextView title;
    TextView author;
    TextView tags;
    ImageView image;
    String link;
    String userID;


    Photo intentContent;


    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        activateToolbar(true);
        Intent intent = getIntent();
        intentContent = (Photo) intent.getSerializableExtra(PHOTO_TRANSFER);

        if (intentContent!=null){
            title = findViewById(R.id.photo_title);
            author = findViewById(R.id.photo_Author);
            tags = findViewById(R.id.photo_tags);
            image = findViewById(R.id.photo_image);

            link = intentContent.getUrl();

            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
            userID = mAuth.getUid();
            title.setText(intentContent.getTitle());
            author.setText(intentContent.getAuthor());
            tags.setText(intentContent.getTags());

            Picasso.with(this).load(intentContent.getUrl())
                    .error(R.drawable.ic_launcher_background)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(image);
        }

        fab = findViewById(R.id.savePhotoButton);
        fab.show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    storeImage();
                    savePhotoDetails();
                    fab.hide();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void storeImage() throws JSONException {

        String link = "http://flickerch-backend.herokuapp.com/database/" + userID;

        String title = intentContent.getTitle();
        String author = intentContent.getAuthor();
        String url = intentContent.getUrl();
        String tags = intentContent.getTags();

        String jsonInputString = "{\"title\": \"" + title +"\", " + "\"author\": \"" + author +"\", " + "\"url\": \"" + url +"\", " + "\"tags\": \"" + tags +"\" }";

        JSONObject req = new JSONObject(jsonInputString);
        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, link, req, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.w("storeimage", response.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void savePhotoDetails() throws JSONException {
        String link = "http://flickerch-backend.herokuapp.com/imagedetail";
        String url = intentContent.getUrl();
        String jsonInputString = "{\"image\": \"" + url +"\"}";
        JSONObject sreq = new JSONObject(jsonInputString);
        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, link, sreq,null , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("photoDetail", error.getMessage());
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(mJsonObjectRequest);
    }
}
