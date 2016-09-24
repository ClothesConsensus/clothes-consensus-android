package com.fadetoproductions.rvkn.clothesconsensus.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by sdass on 9/20/16.
 */
// abbreviated tag name
public class RegistrationIntentService extends IntentService {

    // abbreviated tag name
    private static final String TAG = "RegIntentService";
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String FCM_TOKEN = "FCMToken";
    String BASE_API_URL = "https://clothes-consensus-api.herokuapp.com/";
    String REGISTER_ENDPOINT = "register-device/";
    String USER_ID = "";


    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        USER_ID = intent.getStringExtra("user_id");
        String fcmToken = sharedPreferences.getString(FCM_TOKEN, "");
        if (fcmToken.isEmpty()) {
            // Make a call to Instance API
            FirebaseInstanceId instanceID = FirebaseInstanceId.getInstance();
            // request token that will be used by the server to send push notifications
            String token = instanceID.getToken();
            Log.d(TAG, "FCM Registration Token: " + token);
            sharedPreferences.edit().putString(FCM_TOKEN, token).apply();
        }

        // If user id is null then the intent is Coming from onRefreshToken.
        // Don't send information to server right now.
        if (fcmToken != null && USER_ID != null) {
            // pass along this data
            sendRegistrationToServer(fcmToken);
        } else {
            Log.d("NULL_TOKEN_OR_USER", "FCM Registration Token is null or USER is not logged in yet.");
        }
    }

    public void sendRegistrationToServer(String token) {
        String url = BASE_API_URL + REGISTER_ENDPOINT ;
        Log.v("register_device", "Registering a device");
        Log.v("network_request", url);

        RequestParams params = new RequestParams();
        params.put("reg_token", token);
        params.put("user_id", USER_ID);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.v("network_request", "The device was registered successfully.");
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.v("network_request", "Failed to register the device.");
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply();
    }
}
