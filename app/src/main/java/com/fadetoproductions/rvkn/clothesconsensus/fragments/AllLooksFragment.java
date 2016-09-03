//package com.fadetoproductions.rvkn.clothesconsensus.fragments;
//
//import android.os.Bundle;
//import android.view.View;
//
///**
// * Created by sdass on 9/2/16.
// */
//public class AllLooksFragment extends LooksFragment {
//    public static AllLooksFragment newInstance() {
//        AllLooksFragment allLooksFragment = new AllLooksFragment();
//        return allLooksFragment;
//    }
//    public void populateLooks() {
////        AsyncHttpClient client = new AsyncHttpClient();
////        RequestParams params = new RequestParams();
////        params.put("count", "25");
////        client.get("http://www.clothesconsensus.com", params, new JsonHttpResponseHandler() {
////
////                    @Override
////                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
////                        super.onSuccess(statusCode, headers, response);
////                        looks = Look.fromJsonArray(response);
////                        fillUpAdapterWithData(looks);
////
////                    }
////
////                    @Override
////                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
////                        super.onFailure(statusCode, headers, throwable, errorResponse);
////                    }
////                }
////        );
//
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        populateLooks();
//    }
//}
