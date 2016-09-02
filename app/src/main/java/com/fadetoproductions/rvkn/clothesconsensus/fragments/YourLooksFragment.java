package com.fadetoproductions.rvkn.clothesconsensus.fragments;

import android.os.Bundle;
import android.view.View;

import com.fadetoproductions.rvkn.clothesconsensus.models.Look;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;

/**
 * Created by sdass on 9/2/16.
 */
public class  YourLooksFragment extends LooksFragment{


    public static YourLooksFragment newInstance(String userId) {
        YourLooksFragment  yourLooksFragment = new YourLooksFragment();
        Bundle args = new Bundle();
        args.putString("user_id",userId);
        return yourLooksFragment;
    }
    public void populateLooks() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("count", "25");
        params.put("user_id",getArguments().get("user_id"));
        client.get("http://www.clothesconsensus.com", params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        looks = Look.fromJsonArray(response);
                        fillUpAdapterWithData(looks);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                }
        );

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateLooks();
    }
}
