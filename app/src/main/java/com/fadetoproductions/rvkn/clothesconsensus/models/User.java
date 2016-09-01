package com.fadetoproductions.rvkn.clothesconsensus.models;

import java.util.ArrayList;

/**
 * Created by sdass on 8/31/16.
 */
public class User {
    private ArrayList<Look> looks;
    private ArrayList<Vote> votes;
    private long userId;

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
}
