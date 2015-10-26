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
        client.getHomeTimeline_withCount(1L, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                aTweets.clear();
                tweets.addAll(Tweet.fromJsonArray(response));
                aTweets.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.v("DEBUG Fail:", errorResponse.toString());

            }
        });
    }

    public void customLoadMoreDataFromClient(int page) {
        Tweet tweet = tweets.get(page); //Get the last tweet
        long maxId = Tweet.maxId - 1;


        client.getHomeTimeline_withMaxId(maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                tweets.addAll(Tweet.fromJsonArray(response));
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
                startActivityForResult(intent, ComposeTweetActivity.REQUEST_CODE);

            }
            default: return super.onOptionsItemSelected(item);


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ComposeTweetActivity.REQUEST_CODE && resultCode == RESULT_OK) {

        }
    }
}
