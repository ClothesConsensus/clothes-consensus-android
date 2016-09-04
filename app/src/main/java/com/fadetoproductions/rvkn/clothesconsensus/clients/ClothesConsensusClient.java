package com.fadetoproductions.rvkn.clothesconsensus.clients;

import android.graphics.Bitmap;
import android.util.Log;

import com.fadetoproductions.rvkn.clothesconsensus.models.Look;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.Base64;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rnewton on 9/3/16.
 */


public class ClothesConsensusClient {

    public interface ClothesConsensusClientListener {
        void onGetLooks(ArrayList<Look> looks);
        void onPostLook(JSONObject response);
    }


    String BASE_API_URL = "https://clothes-consensus-api.herokuapp.com/";
    String LOOKS_ENDPOINT = "looks/";


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
