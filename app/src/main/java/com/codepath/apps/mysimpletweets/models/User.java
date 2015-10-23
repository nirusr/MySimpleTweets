package com.codepath.apps.mysimpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sgovind on 10/23/15.
 */
public class User {
    //list attributes
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    //deserialize json object

    public static User fromJson(JSONObject json) {
        User u  = new User();
        try {
            u.name = json.getString("name");
            u.uid = json.getLong("id_str");
            u.screenName = json.getString("screen_name");
            u.profileImageUrl = json.getString("profile_image_url");
        } catch (JSONException e) {
            e.printStackTrace();

        }


        return u;

    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
