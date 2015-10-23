package com.codepath.apps.mysimpletweets.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONObject;

/**
 * Created by sgovind on 10/23/15.
 */
@Table(name = "Tweets")
public class TweetModel extends Model {
    @Column( name = "userId")
    String userId;
    @Column( name = "userHandle")
    String userHandle;
    @Column(name = "timestamp")
    String timestamp;
    @Column(name = "body")
    String body;

    public TweetModel() {
        super();
    }

    public TweetModel(JSONObject object) {
        super();



    }


}
