package com.fadetoproductions.rvkn.clothesconsensus.clients;

import android.util.Log;

import com.fadetoproductions.rvkn.clothesconsensus.models.Look;
import com.fadetoproductions.rvkn.clothesconsensus.models.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rnewton on 9/3/16.
 */


public class ClothesConsensusClient {

    public interface ClothesConsensusClientListener {
        void onGetLooks(ArrayList<Look> looks);
        void onPostLook(JSONObject response);
        void onGetUser(User user);
    }

    String BASE_API_URL = "https://clothes-consensus-api.herokuapp.com/";
    String LOOKS_ENDPOINT = "looks/";
    String USERS_ENDPOINT = "users/";
    String VOTES_ENDPOINT = "votes/";


    public ClothesConsensusClientListener listener;
    private AsyncHttpClient client;

    public void setListener(ClothesConsensusClientListener listener) {
        this.listener = listener;
    }

    public ClothesConsensusClient() {
        client = new AsyncHttpClient();
    }

    public void getLooks() {
        String url = BASE_API_URL + LOOKS_ENDPOINT;
        Log.v("network_request", "Fetching Looks");
        Log.v("network_request", url);

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.v("network_request", "Success");
                ArrayList<Look> looks = Look.fromJsonArray(response);
                listener.onGetLooks(looks);
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.v("network_request", "Failure");
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void getUser(long user_id) {
        String url = BASE_API_URL + USERS_ENDPOINT + user_id + "/";
        Log.v("network_request", "Fetching User");
        Log.v("network_request", url);

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.v("network_request", "Success");
                User user = User.fromJson(response);
                listener.onGetUser(user);
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.v("network_request", "Failure");
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void postLook(long userId, String quote, String expiration, String imageString) {
        // Some user param data
        String url = BASE_API_URL + LOOKS_ENDPOINT;
        Log.v("network_request", "Posting look");
        Log.v("network_request", url);

        // Some compression stuff from http://stackoverflow.com/questions/18545211/bitmap-to-file-that-can-be-uploaded-via-async
        RequestParams params = new RequestParams();
        params.put("image_string", imageString);
        params.put("user_id", userId);
        params.put("quote", quote);
        params.put("expiration", expiration);

        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.v("network_request", "The look was successfully posted");
                listener.onPostLook(response);
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.v("network_request", "The look failed to post");
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }


    public void postVoteForLook(long userId, long lookId, Boolean vote) {
        String url = BASE_API_URL + LOOKS_ENDPOINT + lookId + "/" + VOTES_ENDPOINT;
        Log.v("network_request", "Voting on a look");
        Log.v("network_request", url);

        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("vote", vote);

        client.post(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.v("network_request", "The vote was successful");
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.v("network_request", "The vote failed to post");
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
