package com.fadetoproductions.rvkn.clothesconsensus.clients;

import android.graphics.Bitmap;
import android.util.Log;

import com.fadetoproductions.rvkn.clothesconsensus.models.Look;
import com.fadetoproductions.rvkn.clothesconsensus.models.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.Base64;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
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


    public ClothesConsensusClientListener listener;
    private AsyncHttpClient client;

    public void setListener(ClothesConsensusClientListener listener) {
        this.listener = listener;
    }

    public ClothesConsensusClient() {
        client = new AsyncHttpClient();
    }

    public void getLooks() {
        Log.v("network_request", "Fetching Looks");
        String url = BASE_API_URL + LOOKS_ENDPOINT;

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

    public void getUser(String user_id) {
        Log.v("network_request", "Fetching User");
        String url = BASE_API_URL + USERS_ENDPOINT + user_id;

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
    public void getMyLooks(final String user_id) {
        Log.v("network_request", "Fetching User Looks");
        String url = BASE_API_URL + LOOKS_ENDPOINT;

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.v("network_request", "Success");
                ArrayList<Look> looks = Look.fromJsonArray(response);
                //TODO Temporary: Need to be calculated in back-end.
                looks = filter(looks, user_id);
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

    private ArrayList<Look> filter(ArrayList<Look> looks, String user_id) {
        ArrayList<Look> myLooks = new ArrayList<>();
        for (int i=0; i<looks.size(); i++){
            if(looks.get(i).getUser().getUserId() == Long.parseLong(user_id)){
                myLooks.add(looks.get(i));
            }

        }
        return myLooks;
    }

    public void postLook(Bitmap lookBitmap) {
        // Some user param data
        Log.v("network_request", "Posting look");
        String url = BASE_API_URL + LOOKS_ENDPOINT;

        // Some compression stuff from http://stackoverflow.com/questions/18545211/bitmap-to-file-that-can-be-uploaded-via-async
        RequestParams params = new RequestParams();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        lookBitmap.compress(Bitmap.CompressFormat.PNG, 85, out);
        byte[] myByteArray = out.toByteArray();
        String encodedImage = Base64.encodeToString(myByteArray, Base64.DEFAULT);
        params.put("imageString", encodedImage);

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
}
