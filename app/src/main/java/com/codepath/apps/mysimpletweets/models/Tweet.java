package com.codepath.apps.mysimpletweets.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by sgovind on 10/23/15.
 */
public class Tweet implements Parcelable {

    //List out the attributes
    private String body;
    private long uid;
    private String createdAt;
    private User user;
    public static long maxId;

    //Deserialize JSON
    public static Tweet fromJson(JSONObject jsonObject) {

        Tweet tweet = new Tweet();
        //Extract values from json object
        try {
            //Log.v("DEBUG JSON=", jsonObject.toString());
            tweet.body = jsonObject.getString("text");

            tweet.uid = jsonObject.getLong("id");
            String tweetCreatedAt = jsonObject.getString("created_at");
            tweet.createdAt = Tweet.getRelatvieTimeAgo(tweetCreatedAt);
            tweet.user = (User.fromJson(jsonObject.getJSONObject("user")));



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;
    }

    //Get Tweets from Array
    public static ArrayList<Tweet> fromJsonArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for ( int i = 0; i < jsonArray.length(); i++) {


            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJson(tweetJson);
                if ( tweet != null ) {
                    tweets.add(tweet);
                    maxId = tweet.getUid();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }


        return tweets;
    }

    public static String getRelatvieTimeAgo(String rawJsonDate) {
        String ABBR_YEAR = "y";
        String ABBR_WEEK = "w";
        String ABBR_DAY = "d";
        String ABBR_HOUR = "h";
        String ABBR_MINUTE = "m";
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);


        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();

            long span = Math.max(System.currentTimeMillis() - dateMillis, 0);


            if (span >= DateUtils.YEAR_IN_MILLIS) {
                return (span / DateUtils.YEAR_IN_MILLIS) + ABBR_YEAR;
            }
            if (span >= DateUtils.WEEK_IN_MILLIS) {
                return (span / DateUtils.WEEK_IN_MILLIS) + ABBR_WEEK;
            }
            if (span >= DateUtils.DAY_IN_MILLIS) {
                return (span / DateUtils.DAY_IN_MILLIS) + ABBR_DAY;
            }
            if (span >= DateUtils.HOUR_IN_MILLIS) {
                return (span / DateUtils.HOUR_IN_MILLIS) + ABBR_HOUR;
            }
            return (span / DateUtils.MINUTE_IN_MILLIS) + ABBR_MINUTE;


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";

    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static long getMaxId() {
        return maxId;
    }

    public static void setMaxId(long maxId) {
        Tweet.maxId = maxId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.body);
        dest.writeLong(this.uid);
        dest.writeString(this.createdAt);
        dest.writeParcelable(this.user, flags);
    }

    public Tweet() {
    }

    protected Tweet(Parcel in) {
        this.body = in.readString();
        this.uid = in.readLong();
        this.createdAt = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Parcelable.Creator<Tweet> CREATOR = new Parcelable.Creator<Tweet>() {
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }

        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
}
