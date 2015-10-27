package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.codepath.apps.mysimpletweets.Helper.DBSaveAsyncTask;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.application.TwitterApplication;
import com.codepath.apps.mysimpletweets.client.TwitterClient;
import com.codepath.apps.mysimpletweets.listeners.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.TweetModel;
import com.loopj.android.http.JsonHttpResponseHandler;


import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.ArrayList;

//branch
public class TimelineActivity extends AppCompatActivity {
    public TwitterClient client;
    public TweetsArrayAdapter aTweets;
    public RecyclerView lvTweets;
    public ArrayList<Tweet> tweets;
    public SwipeRefreshLayout swipeContainer;

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

        //swipecontainer
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //populateTimeline();
                populateTimeline();
                swipeContainer.setRefreshing(false);

            }
        });

        //Listener
        lvTweets.addOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromClient(page);
                return true;
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



         populateTimeline();

    }
    //Send API Request
    //Fill the listview with results
    private void populateTimeline() {

        if ( isNetworkAvailable()) {
            client.getHomeTimeline_withCount(1L, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    aTweets.clear();
                    aTweets.addAll(Tweet.fromJsonArray(response));
                    Log.d("Initial Fetch=", Long.toString(aTweets.getItemCount()));
                    new Delete().from(TweetModel.class).execute();
                    new DBSaveAsyncTask().execute(Tweet.fromJsonArray(response));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                    if (! isNetworkAvailable()) {
                        Toast.makeText(getApplicationContext(), "Network Not available - Getting Data from Saved DB", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.v("DEBUG Fail:", errorResponse.toString());
                    }

                }
            });
        } else {
            //TODO get data from DB
            Toast.makeText(this, "Network Not available - Getting Data from Saved DB", Toast.LENGTH_SHORT).show();

        }
    }

    public void customLoadMoreDataFromClient(int page) {
        Tweet tweet = tweets.get(page); //Get the last tweet
        long maxId = Tweet.maxId - 1;

        if ( isNetworkAvailable()) {
            client.getHomeTimeline_withMaxId(maxId, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    aTweets.addAll(Tweet.fromJsonArray(response));
                    Log.d("Scroll Fetch=", Long.toString(aTweets.getItemCount()));
                    new DBSaveAsyncTask().execute(Tweet.fromJsonArray(response));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    if (! isNetworkAvailable()) {
                        Toast.makeText(getApplicationContext(), "Network Not available - Getting Data from Saved DB", Toast.LENGTH_SHORT).show();
                    } else {
                       Log.v("DEBUG Fail:", errorResponse.toString());
                    }

                }
            });
        } else {
            Toast.makeText(this, "Network Not available - Getting Data from Saved DB", Toast.LENGTH_SHORT).show();

        }
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

    public Boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return ( (activeNetworkInfo != null) && activeNetworkInfo.isConnectedOrConnecting());



    }
}
