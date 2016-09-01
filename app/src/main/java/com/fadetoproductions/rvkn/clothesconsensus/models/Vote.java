package com.fadetoproductions.rvkn.clothesconsensus.models;

/**
 * Created by sdass on 8/31/16.
 */
public class Vote {


    private User user;
    private Look look;
    private int rating;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Look getLook() {
        return look;
    }

    public void setLook(Look look) {
        this.look = look;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
