package com.fadetoproductions.rvkn.clothesconsensus.clients;

import android.util.Log;

import com.fadetoproductions.rvkn.clothesconsensus.models.Look;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rnewton on 9/3/16.
 */


public class ClothesConsensusClient {

    public interface ClothesConsensusClientListener {
        void onGetLooks(ArrayList<Look> looks);
    }


    String BASE_API_URL = "https://clothes-consensus-api.herokuapp.com/";
    String LOOKS_ENDPOINT = "looks/";


    public ClothesConsensusClientListener listener;

    public void setListener(ClothesConsensusClientListener listener) {
        this.listener = listener;
    }

    public ClothesConsensusClient() {
    }


    public void getLooks() {
        String url = BASE_API_URL + LOOKS_ENDPOINT;
        AsyncHttpClient client = new AsyncHttpClient();
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



}
