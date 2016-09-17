package com.fadetoproductions.rvkn.clothesconsensus.models;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

/**
 * Created by rnewton on 9/17/16.
 */
public class CameraSettings {

    // TODO should probably use enums here for each of these settings instead
    Boolean useBackCamera;
    Boolean flashOn;

    public void toggleFlash() {
        flashOn = !flashOn;
    }

    public void toggleCamera() {
        useBackCamera = !useBackCamera;
    }


    public Boolean getUseBackCamera() {
        return useBackCamera;
    }

    public void setUseBackCamera(Boolean useBackCamera) {
        this.useBackCamera = useBackCamera;
    }

    public Boolean getFlashOn() {
        return flashOn;
    }

    public void setFlashOn(Boolean flashOn) {
        this.flashOn = flashOn;
    }

    public Boolean getSingleLook() {
        return singleLook;
    }

    public void setSingleLook(Boolean singleLook) {
        this.singleLook = singleLook;
    }

    Boolean singleLook;

    public CameraSettings() {
        useBackCamera = true;
        flashOn = true;
        singleLook = true;
    }

    public static void setCameraSettings(Activity activity, CameraSettings cameraSettings) {
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = mSettings.edit();

        Gson gson = new Gson();
        String json = gson.toJson(cameraSettings);

        editor.putString("cameraSettings", json);
        editor.commit();
    }

    public static CameraSettings getCameraSettings(Activity activity) {
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(activity);
        Gson gson = new Gson();
        String json = mSettings.getString("cameraSettings", "");

        // If we don't have any yet, we should set them
        if (json.isEmpty()) {
            CameraSettings cameraSettings = new CameraSettings();
            setCameraSettings(activity, cameraSettings);
            return cameraSettings;
        }

        CameraSettings cameraSettings = gson.fromJson(json, CameraSettings.class);
        return cameraSettings;
    }
}
