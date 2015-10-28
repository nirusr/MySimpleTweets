package com.codepath.apps.mysimpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sgovind on 10/27/15.
 */
public class Credential {

    private String myName;
    private String myImageUrl;


    public static Credential fromJsonObject(JSONObject jsonObject) {
        Credential myCredential = new Credential();
        try {
            myCredential.setMyName(jsonObject.getString("name"));
            myCredential.setMyImageUrl(jsonObject.getString("profile_image_url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return myCredential;

    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getMyImageUrl() {
        return myImageUrl;
    }

    public void setMyImageUrl(String myImageUrl) {
        this.myImageUrl = myImageUrl;
    }
}
