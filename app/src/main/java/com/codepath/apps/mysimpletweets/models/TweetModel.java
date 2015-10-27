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
    @Column(name = "Created_At")
    private  String timestamp;
    @Column(name = "Body")
    private String body;
    @Column ( name = "Tweet_Id", index =  true)
    private long uid;
    @Column ( name = "Max_ID")
    private long max_id;
    @Column (name = "User" , onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE )
    private UserModel user;


    //Default Constructor
    public TweetModel() {

    }

    public TweetModel(String timestamp, String body, long uid, long max_id) {
        this.timestamp = timestamp;
        this.body = body;
        this.uid = uid;
        this.max_id = max_id;
    }


    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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

    public long getMax_id() {
        return max_id;
    }

    public void setMax_id(long max_id) {
        this.max_id = max_id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
