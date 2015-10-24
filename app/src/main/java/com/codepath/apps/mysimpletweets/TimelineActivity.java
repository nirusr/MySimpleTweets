package com.codepath.apps.mysimpletweets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;


import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class TimelineActivity extends AppCompatActivity {
    public TwitterClient client;
    public TweetsArrayAdapter aTweets;
    public RecyclerView lvTweets;
    public ArrayList<Tweet> tweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        lvTweets = (RecyclerView) findViewById(R.id.lvTweets);

        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsArrayAdapter(tweets);
        lvTweets.setAdapter(aTweets);
        lvTweets.setLayoutManager(new LinearLayoutManager(this));

        client = TwitterApplication.getRestClient();//Create singleton client

        populateTimeline();
    }
    //Send API Request
    //Fill the listview with results
    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //super.onSuccess(statusCode, headers, response);
                //Log.v("DEBUG Success:", response.toString());
                //Desserialize json
               // aTweets.addAll(Tweet.fromJsonArray(response));
                aTweets.clear();
                tweets.addAll(Tweet.fromJsonArray(response));
                /*
                ArrayList<Tweet> tweetsArrayList = Tweet.fromJsonArray(response);
                for ( Tweet t : tweetsArrayList) {
                    tweets.add(t);
                    Log.d("Added:", t.getUser().getName());
                }*/

                aTweets.notifyDataSetChanged();
             }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.v("DEBUG Fail:", errorResponse.toString());
            }
        });
   }
}
