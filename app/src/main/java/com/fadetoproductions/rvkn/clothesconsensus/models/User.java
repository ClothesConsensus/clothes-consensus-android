package com.fadetoproductions.rvkn.clothesconsensus.models;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

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
    private String bannerImageUrl;
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
            user.profileImageUrl = "https://clothes-consensus-api.herokuapp.com" + object.getString("profile_image");
            user.bannerImageUrl = "https://clothes-consensus-api.herokuapp.com" + object.getString("banner_image");
            user.name = object.getString("name");

            if (object.has("looks")) {
                user.looks = Look.fromJsonArray(object.getJSONArray("looks"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }


    public static void setLoggedInUser(Activity activity, User user) {
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = mSettings.edit();

        Gson gson = new Gson();
        String json = gson.toJson(user);

        editor.putString("loggedInUser", json);
        editor.commit();
    }

    public static User getLoggedInUser(Activity activity) {
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(activity);
        Gson gson = new Gson();
        String json = mSettings.getString("loggedInUser", "");
        if (json.isEmpty()) {
            return null;
        }

        User user = gson.fromJson(json, User.class);
        return user;
    }
}
