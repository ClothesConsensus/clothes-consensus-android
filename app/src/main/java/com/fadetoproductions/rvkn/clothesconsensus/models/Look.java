package com.fadetoproductions.rvkn.clothesconsensus.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sdass on 8/31/16.
 */
public class Look {

    private long userId;
    private String lookId;
    private String photoUrl;
    private ArrayList<Vote> votes;
    private String thumbnailImage;
    private String message;
    public long findAverageRating(){
        long average;
        int sum = 0;
        for(int i=0; i<votes.size(); i++){
            sum += votes.get(i).getRating();
        }
        average = sum/votes.size();
        return  average;
    }
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getLookId() {
        return lookId;
    }

    public void setLookId(String lookId) {
        this.lookId = lookId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public ArrayList<Vote> getVotes() {
        return votes;
    }

    public void setVotes(ArrayList<Vote> votes) {
        this.votes = votes;
    }


    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public String getMessage() {
        return message;
    }

    public static Look fromJson(JSONObject object) {
        Look look = new Look();
        try {
            look.lookId = object.getString("look_id");
            look.photoUrl = object.getString("photo_url");
            look.thumbnailImage = object.getString("thumbnail_image");
            look.message = object.getString("message");



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return look;
    }

    public static ArrayList<Look> fromJsonArray(JSONArray jsonArray) {
        ArrayList<Look> looks = new ArrayList<>();
        for (int i=0; i<jsonArray.length();i++) {
            try {
                Look tweet = Look.fromJson(jsonArray.getJSONObject(i));
                if(tweet != null) {
                    looks.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return looks;
    }
}
