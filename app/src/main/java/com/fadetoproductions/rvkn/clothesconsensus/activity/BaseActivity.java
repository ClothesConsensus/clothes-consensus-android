package com.fadetoproductions.rvkn.clothesconsensus.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.clients.ClothesConsensusClient;
import com.fadetoproductions.rvkn.clothesconsensus.models.Look;
import com.fadetoproductions.rvkn.clothesconsensus.models.User;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by rnewton on 9/4/16.
 *
 * Using this class so that we don't have to repeat code, especially for activity switching
 *
 */
public class BaseActivity extends AppCompatActivity implements ClothesConsensusClient.ClothesConsensusClientListener {

    // TODO move this camera stuff
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final static int CAMERA_PERMISSIONS_REQUEST_GRANTED = 9999;

    ClothesConsensusClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new ClothesConsensusClient();
        client.setListener(this);

    }

    private Boolean checkAndRequestCameraPermissions() {
        // https://developer.android.com/training/permissions/requesting.html
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                return false;
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSIONS_REQUEST_GRANTED);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST_GRANTED: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadCamera();
                } else {

                }
                return;
            }
        }
    }

    public void loadCamera() {
        Log.v("action", "Loading camera 2");
        Boolean hasCameraPermissions = checkAndRequestCameraPermissions();
        if (!hasCameraPermissions) {
            return;
        }

        Intent i = new Intent(this, CameraActivity.class);
        startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

////         This is all from the guide
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, PhotoUtils.getPhotoFileUri(this, PhotoUtils.PHOTO_FILE_NAME));
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            // Start the image capture intent to take photo
//            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
//        }
    }

    public void back() {
        finish();
    }

    public void loadProfileForUser(User user) {
        Log.v("action", "Loading user profile");
        Intent i = new Intent(this, ProfileV2Activity.class);
        i.putExtra("user", Parcels.wrap(user));
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void loadLookConfirmationScreen() {
        // TODO this should become part of the custom camera activity
        Intent i = new Intent(this, LookConfirmationActivity.class);
        Log.v("action", "Starting look confirmation screen");
        startActivity(i);
        overridePendingTransition(R.anim.no_change, R.anim.slide_out_right);
    }

    // TODO this probably shouldn't be in the base activity. We can move it out when we create custom camera view
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.v("action", "Photo taken!!");
                loadLookConfirmationScreen();

            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    // The child activity should implement this
    @Override
    public void onGetLooks(ArrayList<Look> looks) {}

    // The child activity should implement this
    @Override
    public void onPostLook(JSONObject response) {}

    // The child activity should implement this
    @Override
    public void onGetUser(User user) {
    }
}
