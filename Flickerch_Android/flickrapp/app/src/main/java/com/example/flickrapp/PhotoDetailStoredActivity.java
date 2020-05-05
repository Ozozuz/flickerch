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

public class PhotoDetailStoredActivity extends BaseActivity {
    TextView title;
    TextView author;
    TextView tags;
    ImageView image;
    String link;
    String userID;

    TextView WHB;

    Photo intentContent;


    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail_stored);
        activateToolbar(true);
        Intent intent = getIntent();
        intentContent = (Photo) intent.getSerializableExtra(PHOTO_TRANSFER);

        if (intentContent!=null){
            try {
                setPhotoDetail();
            } catch (JSONException e){}
            title = findViewById(R.id.photo_title);
            author = findViewById(R.id.photo_Author);
            tags = findViewById(R.id.photo_tags);
            image = findViewById(R.id.photo_image);
            WHB     = findViewById(R.id.photo_WHB);

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
    }

    private void setPhotoDetail() throws JSONException {
        String link = "http://flickerch-backend.herokuapp.com/imagedetail";
        String url = intentContent.getUrl();
        String jsonInputString = "{\"image\": \"" + url +"\"}";
        JSONObject req = new JSONObject(jsonInputString);
        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, link, req, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    WHB.setText(response.getString("sizeW") + "*" +response.getString("sizeH") + "  "+ response.getString("sizeB"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("photoDetail", error.getMessage());
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(mJsonObjectRequest);
    }
}
