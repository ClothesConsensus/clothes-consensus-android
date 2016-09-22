package com.fadetoproductions.rvkn.clothesconsensus.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by sdass on 9/20/16.
 */
// abbreviated tag name
public class RegistrationIntentService extends IntentService {

    // abbreviated tag name
    private static final String TAG = "RegIntentService";
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String FCM_TOKEN = "FCMToken";


    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Make a call to Instance API
        FirebaseInstanceId instanceID = FirebaseInstanceId.getInstance();
        String senderId = getResources().getString(R.string.gcm_defaultSenderId);
        // request token that will be used by the server to send push notifications
            // Token for now :  fP6-4ytom4I:APA91bF7G1yCOSCTYZKlDwn1qg3FncfL_ixYxBcF7L-BthpnCUQiufDhaYsdVG11bMZscdFcWOYlI4knRqLlK26UkM3CkJEBjXD3lSGxzOt68gyV0XZes-ZJNuWO8-ihfnHot5GEmD9s
        String token = instanceID.getToken();
        Log.d(TAG, "FCM Registration Token: " + token);

        // pass along this data
        // save token
        sharedPreferences.edit().putString(FCM_TOKEN, token).apply();
        // pass along this data
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply();

    }
}
