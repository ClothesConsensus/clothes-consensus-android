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
    private Long lookId;
    private String photoUrl;
    private ArrayList<Vote> votes;
    private String message;
    private Integer votesYes;
    private Integer votesNo;


    public User getUser() {
        return user;
    }


    public long findAverageRating(){
        return 0;
    }

    public Long getLookId() {
        return lookId;
    }


    public String getPhotoUrl() {
        return photoUrl;
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
            look.lookId = object.getLong("id");
            look.photoUrl = "https://clothes-consensus-api.herokuapp.com/" + object.getString("image_url");
            look.message = object.getString("quote");
            JSONObject userObject = object.getJSONObject("user");
            look.user = User.fromJson(userObject);

            JSONObject voteResults = object.getJSONObject("vote_results");
            look.votesYes = voteResults.getInt("yes");
            look.votesNo = voteResults.getInt("no");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return look;
    }

    public static ArrayList<Look> fromJsonArray(JSONArray jsonArray) {
        /** CURRENT JSON STRUCTURE
         [{
             "id": 78,
             "user_id": 24,
             "image_url": "/looks/20.jpg",
             "quote": "Is this a cool style?",
             "expiration": "2017-01-01T01:01:01.000Z",
             "type_index": 0,
             "vote_results": {
                 "yes": 18,
                 "no": 43
             },
             "user": {
                 "id": 24,
                 "name": "Joe",
                 "profile_image": "/user-thumbnails/11.jpg",
                 "banner_image": "/user-banners/11.jpg"
                 }
             },
             {
             "id": 100,
             "user_id": 23,
             "image_url": "/looks/78.jpg",
             "quote": "Is this a good pattern?",
             "expiration": "2017-01-01T01:01:01.000Z",
             "type_index": 0,
             "vote_results": {
                 "yes": 49,
                 "no": 3
             },
             "user": {
                 "id": 23,
                 "name": "Amanda",
                 "profile_image": "/user-thumbnails/10.jpg",
                 "banner_image": "/user-banners/10.jpg"
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

    public boolean isCurrent() {
        //TODO find whether timer expired or not.
        return true;
    }
}
