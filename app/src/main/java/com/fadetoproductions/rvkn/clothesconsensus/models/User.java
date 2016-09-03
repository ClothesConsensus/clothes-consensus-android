package com.fadetoproductions.rvkn.clothesconsensus.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by sdass on 8/31/16.
 */
@Parcel(analyze={User.class})
public class User {
    private ArrayList<Look> looks;
    private ArrayList<Vote> votes;
    private long userId;
    private String profileImageUrl;
    private String name;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public ArrayList<Look> getLooks() {
        return looks;
    }

    public void setLooks(ArrayList<Look> looks) {
        this.looks = looks;
    }

    public ArrayList<Vote> getVotes() {
        return votes;
    }

    public void setVotes(ArrayList<Vote> votes) {
        this.votes = votes;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }


    public static User fromJson(JSONObject object) {
        User user = new User();
        try {
            user.userId = object.getInt("id");
            user.profileImageUrl = object.getString("photo_thumbnail");
            user.name = object.getString("name");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

}
