package com.fadetoproductions.rvkn.clothesconsensus.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by sdass on 8/31/16.
 */
@Parcel(analyze={Look.class})
public class Look {


    private User user;
    private String lookId;
    private String photoUrl;
    private ArrayList<Vote> votes;
    private String message;
    private String hour;
    private String minute;

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public User getUser() {
        return user;
    }


    public long findAverageRating(){
        return 0;
//        long average;
//        int sum = 0;
//        for(int i=0; i<votes.size(); i++){
//            sum += votes.get(i).getRating();
//        }
//        average = sum/votes.size();
//        return average;
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

    public String getMessage() {
        return message;
    }

    public static Look fromJson(JSONObject object) {
        Look look = new Look();
        try {
            look.lookId = object.getString("id");
            look.photoUrl = object.getString("photo_url");
            look.message = object.getString("message");
            JSONObject userObject = object.getJSONObject("user");
            look.user = User.fromJson(userObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return look;
    }

    public static ArrayList<Look> fromJsonArray(JSONArray jsonArray) {
        /** CURRENT JSON STRUCTURE
         [{
             "id": 1,
             "photo_url": "http://localhost:4567/100.jpg",
             "message": "Is this good for business casual?",
             "user": {
                 "id": 3,
                 "photo_thumbnail": "http://localhost:4567/user-thumbnails/3.jpg",
                 "name": "Shashank"
             }
         },
         {
             "id": 2,
             "photo_url": "http://localhost:4567/101.jpg",
             "message": "Does this fit?",
             "user": {
                 "id": 2,
                 "photo_thumbnail": "http://localhost:4567/user-thumbnails/2.jpg",
                 "name": "Ryan"
             }
         }]
         */

        ArrayList<Look> looks = new ArrayList<>();
        for (int i=0; i < jsonArray.length(); i++) {
            try {
                Look look = Look.fromJson(jsonArray.getJSONObject(i));
                if(look != null) {
                    looks.add(look);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return looks;
    }
}
