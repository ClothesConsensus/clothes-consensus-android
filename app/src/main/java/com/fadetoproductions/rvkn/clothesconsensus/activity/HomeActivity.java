package com.fadetoproductions.rvkn.clothesconsensus.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.adapter.LookAdapter;
import com.fadetoproductions.rvkn.clothesconsensus.databinding.ActivityHomeBinding;
import com.fadetoproductions.rvkn.clothesconsensus.models.Look;
import com.fadetoproductions.rvkn.clothesconsensus.utils.DividerItemDecoration;
import com.fadetoproductions.rvkn.clothesconsensus.utils.EndlessRecyclerViewScrollListener;
import com.fadetoproductions.rvkn.clothesconsensus.utils.ItemClickSupport;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class HomeActivity extends AppCompatActivity {
    RecyclerView rvLooks;
    protected ArrayList<Look> looks;
    LookAdapter lookAdapter;
    ActivityHomeBinding activityHomeBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = DataBindingUtil.setContentView(this,R.layout.activity_home);
        rvLooks = activityHomeBinding.rvLooks;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvLooks.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        rvLooks.addItemDecoration(itemDecoration);
        ItemClickSupport.addTo(rvLooks).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                    }
                }
        );
        rvLooks.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
//                customLoadMoreDataFromApi(page);
            }
        });
        bindDataToAdapter();
        populateLooks();
        //Network call here to fetch the looks.
        //Make the model
        //

    }

    public void populateLooks() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("count", "25");
        client.get("http://www.clothesconsensus.com", params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        ArrayList<Look> looks = Look.fromJsonArray(response);
                        fillUpAdapterWithData(looks);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                }
        );

    }
    protected void bindDataToAdapter() {
        // Bind adapter to recycler view object
        lookAdapter = new LookAdapter(this, looks);
        rvLooks.setAdapter(lookAdapter);
    }
    protected void fillUpAdapterWithData(ArrayList<Look> looks) {
        lookAdapter.addAll(looks);
    }
}
