package com.example.flickrapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.text.style.IconMarginSpan;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class GetFlickJsonData extends AsyncTask<String, Void, List<Photo>>{
    private static final String TAG = "GetFlickJsonData";
    private List<Photo> photos = null;
    private String base_url;
    private String language;
    private boolean matchAll;
    private final OnJsonDataAvailable callBack;

    interface OnJsonDataAvailable{
        void onJsonDataAvailable(List<Photo> photo, String result);
    }

    public GetFlickJsonData(String base_url, String mlanguage, boolean matchAll, OnJsonDataAvailable callBack) {
        this.base_url = base_url;
        this.language = mlanguage;
        this.matchAll = matchAll;
        this.callBack = callBack;
    }

    @Override
    protected List<Photo> doInBackground(String... searchCriteria) {
        try {
            //////////////////////////////////////////////////////////////////
            String responseLine;
            String link = "http://flickerch-backend.herokuapp.com/search/" + searchCriteria[0];
            URL url = new URL (link);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            InputStreamReader streamReader = new InputStreamReader(con.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder response = new StringBuilder();

            while ((responseLine = reader.readLine()) != null) {
                response.append(responseLine.trim());
            }

            fromJSONtoPhoto(response.toString());
            /////////////////////////////////////////////////////////////////
        }
        catch (Exception e){
            Log.d(TAG, "MyBackendERROR:" + e.getMessage());
        }
        return photos;
    }

    private void fromJSONtoPhoto(String data){
        this.photos = new ArrayList<>();
        try{
            JSONObject jsonData = new JSONObject(data);
            JSONArray itemsArray = jsonData.getJSONArray("photos");
            for(int i=0; i < itemsArray.length(); i++){

                JSONObject obj = itemsArray.getJSONObject(i);
                String title = obj.getString("title");
                String author = obj.getString("author");
                String tags = obj.getString("tags");
                String url = obj.getString("url");

                Photo tmpPhoto = new Photo(title,author,url, tags);
                photos.add(tmpPhoto);
            }
        }
        catch (JSONException e){
            Log.e(TAG, "MyBackendERROR: "+e.getMessage() );
        }
    }
    @Override
    protected void onPostExecute(List<Photo> photos) {
        if (this.callBack != null )
            callBack.onJsonDataAvailable(photos, "SUCCESS");
    }

}