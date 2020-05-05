package com.example.flickrapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;

public class MainActivity extends BaseActivity implements GetFlickJsonData.OnJsonDataAvailable,
                                                                RecyclerItemClickListener.OnRecyclerClickListener{
    private static final String TAG = "MainActivity";
    private final String DEFAULT_TAG = "forest";
    private FlickerRecyclerViewAdapter flickerRecyclerViewAdapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //////////////////////////////////////////
        //////////////////////////////////////////
        activateToolbar(false);

        RecyclerView recyclerView = findViewById(R.id.recycler_view123);
        recyclerView.setLayoutManager(new LinearLayoutManager(this ));

        RecyclerItemClickListener clickListener = new RecyclerItemClickListener(this, recyclerView, this);
        recyclerView.addOnItemTouchListener( clickListener );

        flickerRecyclerViewAdapter = new FlickerRecyclerViewAdapter(this, new ArrayList<Photo>());
        recyclerView.setAdapter(flickerRecyclerViewAdapter);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        String queryResult = sharedPreferences.getString(FLICKR_QUERY, "");

        if(queryResult==null || queryResult.length()==0) {
            Log.d("ROTATION", queryResult);
            queryResult=DEFAULT_TAG;
        }

        GetFlickJsonData getFlickJsonDataAsync =
                new GetFlickJsonData("https://www.flickr.com/services/feeds/photos_public.gne",
                        "en-en", true, this );
        getFlickJsonDataAsync.execute(queryResult);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id==R.id.action_search){
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id==R.id.logoutMenu){
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            Intent intent = new Intent(getBaseContext(), Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else if(id==R.id.imgsSavedMenu){
            Intent intent = new Intent(getBaseContext(), SavedImages.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onJsonDataAvailable(List<Photo> photos, String status) {
        if(status.equals("SUCCESS")) {
            flickerRecyclerViewAdapter.loadNewData(photos);
        }
        else
            Log.e(TAG, "onJsonDataAvailable: FAILURE");
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, PhotoDetailActivity.class);
        intent.putExtra(PHOTO_TRANSFER, flickerRecyclerViewAdapter.getPhoto(position));
        startActivity(intent);

    }

    @Override
    public void onItemLongClick(View view, int position) {
    }
}

