package com.codepath.apps.mysimpletweets.Helper;

import android.os.AsyncTask;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.TweetModel;
import com.codepath.apps.mysimpletweets.models.UserModel;

import java.util.ArrayList;

/**
 * Created by sgovind on 10/26/15.
 */
public class DBSaveAsyncTask extends AsyncTask<ArrayList<Tweet>, Void, Boolean> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(ArrayList<Tweet>... lists) {
        ActiveAndroid.beginTransaction();
        ArrayList<Tweet> tweets = lists[0];
        try {
            for (Tweet tweet : tweets) {
                TweetModel tweetModel = new TweetModel();
                tweetModel.setUid(tweet.getUid());
                tweetModel.setTimestamp(tweet.getCreatedAt());
                tweetModel.setBody(tweet.getBody());


                //User Model
                UserModel userModel = new UserModel(tweet.getUser().getName(), tweet.getUid(), tweet.getUser().getScreenName(),
                        tweet.getUser().getProfileImageUrl());
                tweetModel.setUser(userModel);

                tweetModel.save();
                Log.d("DEBUG DB SAVE", tweetModel.toString());
            }
            ActiveAndroid.setTransactionSuccessful();
        } catch ( Exception e) {
            e.printStackTrace();
        } finally {
            ActiveAndroid.endTransaction();
        }

       return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }
}

