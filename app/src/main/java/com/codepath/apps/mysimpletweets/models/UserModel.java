package com.codepath.apps.mysimpletweets.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by sgovind on 10/26/15.
 */
@Table( name = "User")
public class UserModel extends Model {
    @Column ( name = "Name")
    private String name;
    @Column ( name = "Tweet_Id", index = true)
    private long uid;
    @Column ( name = "Screen_Name")
    private String screenName;
    @Column ( name = "Profile_Image_Url")
    private String profileImageUrl;

    //Default Constructor
    public UserModel() {
    }

    public UserModel(String name, long uid, String screenName, String profileImageUrl) {
        this.name = name;
        this.uid = uid;
        this.screenName = screenName;
        this.profileImageUrl = profileImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
