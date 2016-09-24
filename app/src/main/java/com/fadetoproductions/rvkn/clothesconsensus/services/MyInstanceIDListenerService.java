package com.fadetoproductions.rvkn.clothesconsensus.services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by sdass on 9/20/16.
 */
public class MyInstanceIDListenerService extends FirebaseInstanceIdService {
    public static final String FCM_TOKEN = "FCMToken";

    public void onTokenRefresh() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().remove(FCM_TOKEN);
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}