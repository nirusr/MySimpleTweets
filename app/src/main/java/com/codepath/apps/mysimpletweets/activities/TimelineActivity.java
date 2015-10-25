package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.application.TwitterApplication;
import com.codepath.apps.mysimpletweets.client.TwitterClient;
import com.codepath.apps.mysimpletweets.listeners.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.models.Tweet;
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


        //Listener
        lvTweets.addOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromClient(page);
                return true;
            }
        });



    }
    //Send API Request
    //Fill the listview with results
    private void populateTimeline() {
        client.getHomeTimeline(1L, new JsonHttpResponseHandler() {
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

    public void customLoadMoreDataFromClient(int page) {


        Tweet tweet = tweets.get(page); //Get the last tweet
        long last_since_id = tweet.getUid();
        Log.d("DEBUG-ID:", Long.toString(last_since_id));

        client.getHomeTimeline(last_since_id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //super.onSuccess(statusCode, headers, response);
                //Log.v("DEBUG Success:", response.toString());
                //Desserialize json
                // aTweets.addAll(Tweet.fromJsonArray(response));
                //aTweets.clear();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timeline, menu );
        MenuItem addTweetItem = menu.findItem(R.id.action_compose);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_compose: {
                //Toast.makeText(this, "Post clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ComposeTweetActivity.class);
                startActivityForResult(intent, 100);

            }
            default: return super.onOptionsItemSelected(item);


        }




    }
}
