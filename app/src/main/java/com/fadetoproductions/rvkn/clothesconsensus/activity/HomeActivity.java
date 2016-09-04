package com.fadetoproductions.rvkn.clothesconsensus.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.adapter.LooksAdapter;
import com.fadetoproductions.rvkn.clothesconsensus.clients.ClothesConsensusClient;
import com.fadetoproductions.rvkn.clothesconsensus.databinding.ActivityHomeBinding;
import com.fadetoproductions.rvkn.clothesconsensus.databinding.ToolbarBinding;
import com.fadetoproductions.rvkn.clothesconsensus.models.Look;
import com.fadetoproductions.rvkn.clothesconsensus.models.User;
import com.fadetoproductions.rvkn.clothesconsensus.utils.PhotoUtils;

import org.parceler.Parcels;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, ClothesConsensusClient.ClothesConsensusClientListener {

    ActivityHomeBinding activityHomeBinding;
    LooksAdapter adapter;
    ArrayList<Look> looks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
//        setupActionBar();
        looks = new ArrayList<>();
        adapter = new LooksAdapter(this, looks);


        activityHomeBinding.rvLooks.setAdapter(adapter);
        activityHomeBinding.rvLooks.setLayoutManager(new LinearLayoutManager(this));



        //Network call here to fetch the looks.
        //Make the model
        //

        ClothesConsensusClient client = new ClothesConsensusClient();
        client.setListener(this);
        client.getLooks();

        Log.v("network_request", "Fetching Looks");


//        if(savedInstanceState == null) {
//            showAllLooksFragment();
//        }


    }

    private void setupActionBar() {
        ToolbarBinding toolbarBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.toolbar, null, false);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbarBinding.llToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.logo);

        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.background));


        actionBar.setDisplayUseLogoEnabled(true);


    }


//    private void showAllLooksFragment() {
//        AllLooksFragment looksFragment = AllLooksFragment.newInstance();
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(activityHomeBinding.flContainer.getId(), looksFragment);
//        ft.commit();
//    }
    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        // Do something with the tvTime here.

    }
    public void onProfileView(MenuItem item) {
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        // TODO get current user by api calls.
        User user = null;
        profileIntent.putExtra("user_id", Parcels.wrap(user));
        startActivity(profileIntent);
    }

    @Override
    public void onGetLooks(ArrayList<Look> fetchedLooks) {
        looks.addAll(fetchedLooks);
        Log.v("action", "Looks fetched");
        adapter.notifyDataSetChanged();
    }

    public void loadProfile(View view) {
        Log.v("action", "Loading profile");

    }



    // TODO move this camera stuff
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;

    public void loadCamera(View view) {
        Log.v("action", "Loading camera");
        launchCamera();
    }


    public void loadLookConfirmationScreen() {
        // TODO this should become part of the custom camera activity
        Intent i = new Intent(this, LookConfirmationActivity.class);
        Log.v("action", "Starting look confirmation screen");
        startActivity(i);
    }




    @Override
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

    public void launchCamera() {
        // This is all from the guide
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, PhotoUtils.getPhotoFileUri(this, PhotoUtils.PHOTO_FILE_NAME));
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

}
