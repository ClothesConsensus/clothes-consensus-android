package com.fadetoproductions.rvkn.clothesconsensus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.fadetoproductions.rvkn.clothesconsensus.clients.ClothesConsensusClient;
import com.fadetoproductions.rvkn.clothesconsensus.models.Look;
import com.fadetoproductions.rvkn.clothesconsensus.models.User;
import com.fadetoproductions.rvkn.clothesconsensus.utils.PhotoUtils;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by rnewton on 9/4/16.
 *
 * Using this class so that we don't have to repeat code, especially for activity switching
 *
 */
public class BaseActivity extends AppCompatActivity  implements ClothesConsensusClient.ClothesConsensusClientListener {

    // TODO move this camera stuff
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;

    ClothesConsensusClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new ClothesConsensusClient();
        client.setListener(this);

    }

    public void loadCamera() {
        Log.v("action", "Loading camera 2");

//        Intent i = new Intent(this, CameraActivity.class);
//        startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);


        // This is all from the guide
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, PhotoUtils.getPhotoFileUri(this, PhotoUtils.PHOTO_FILE_NAME));
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public void loadProfileForUser(User user) {
        Log.v("action", "Loading user profile");
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("user", Parcels.wrap(user));
        startActivity(i);
    }

    public void loadLookConfirmationScreen() {
        // TODO this should become part of the custom camera activity
        Intent i = new Intent(this, LookConfirmationActivity.class);
        Log.v("action", "Starting look confirmation screen");
        startActivity(i);
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
