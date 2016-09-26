package com.fadetoproductions.rvkn.clothesconsensus.activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.clients.ClothesConsensusClient;
import com.fadetoproductions.rvkn.clothesconsensus.models.Look;
import com.fadetoproductions.rvkn.clothesconsensus.models.User;
import com.fadetoproductions.rvkn.clothesconsensus.services.FCMMessageHandler;
import com.squareup.picasso.Picasso;

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
    ObjectAnimator pbAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new ClothesConsensusClient();
        client.setUser(User.getLoggedInUser(this));
        client.setListener(this);
        cacheUserImages();
    }

    private void cacheUserImages() {
        // TODO just doing this to make the demo smoother
        User user = User.getLoggedInUser(this);
        if (user != null) {
            ImageView view = new ImageView(this);
            Picasso.with(this).load(user.getBannerImageUrl()).into(view);

            ImageView view2 = new ImageView(this);
            Picasso.with(this).load(user.getProfileImageUrl()).into(view2);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Register for the notification broadcast message.
        IntentFilter filter = new IntentFilter(FCMMessageHandler.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(notificationReceiver, filter);
        final RelativeLayout rlNotificationBanner = (RelativeLayout) findViewById(R.id.rlNotificationBanner);
        if (rlNotificationBanner != null) {
            rlNotificationBanner.clearAnimation();
            rlNotificationBanner.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener when the application is paused
        LocalBroadcastManager.getInstance(this).unregisterReceiver(notificationReceiver);
    }

    private BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
            if (resultCode == RESULT_OK) {
                String resultValue = intent.getStringExtra("resultValue");
                Log.v("Notification recieved:", resultValue);

                displayThenHideNotificationBanner(resultValue);
            }
        }
    };

    public void startProgressBar() {
        ProgressBar pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
        if (pbLoading != null) {
            pbLoading.setVisibility(View.VISIBLE);
            pbAnimation = ObjectAnimator.ofInt(pbLoading, "progress", 0, 100);
            pbAnimation.setDuration(1000);
            pbAnimation.setInterpolator(new AccelerateInterpolator());
            pbAnimation.setRepeatCount(ValueAnimator.INFINITE);
            pbAnimation.start();
        }
    }

    public void stopProgressBar() {
        ProgressBar pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
        if (pbLoading != null) {
            if (pbAnimation != null) {
                pbAnimation.cancel();
            }

            ObjectAnimator animation = ObjectAnimator.ofInt(pbLoading, "progress", pbLoading.getProgress(), 100);
            animation.setDuration(100);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.start();
            pbLoading.setVisibility(View.INVISIBLE);
        }
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
        overridePendingTransition(R.anim.slide_up_2, R.anim.no_change);
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

    // TODO this probably shouldn't be in the base activity. We can move it out when we create custom camera view
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

            overridePendingTransition(R.anim.no_change, R.anim.slide_down_2);
            if (resultCode == RESULT_OK) {
                Log.v("action", "Look was uploaded");
                displayThenHideNotificationBanner("Your look was successfully uploaded! We'll notify you when the results are in.");
            } else {
                Log.v("action", "Look was not uploaded");
            }
        }
    }

    // The child activity should implement this
    @Override
    public void onGetLooks(ArrayList<Look> looks) {
        stopProgressBar();
    }

    // The child activity should implement this
    @Override
    public void onPostLook(JSONObject response) {
        stopProgressBar();
    }

    // The child activity should implement this
    @Override
    public void onGetUser(User user) {
        stopProgressBar();
    }

    public void displayThenHideNotificationBanner(String text) {
        final RelativeLayout rlNotificationBanner = (RelativeLayout) findViewById(R.id.rlNotificationBanner);
        if (rlNotificationBanner != null) {
            Log.v("STUFF", "CAAAAAMMMS");

            final User user = User.getLoggedInUser(this);
            TextView tvNotificationText = (TextView) findViewById(R.id.tvNotificationText);
            tvNotificationText.setText(text);

            rlNotificationBanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadProfileForUser(user);
                }
            });

            ScaleAnimation anim = new ScaleAnimation(1, 1, 0, 1);
            anim.setDuration(500);
            rlNotificationBanner.setVisibility(View.VISIBLE);
            rlNotificationBanner.startAnimation(anim);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    ScaleAnimation anim = new ScaleAnimation(1, 1, 1, 0);
                    anim.setDuration(500);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            rlNotificationBanner.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    rlNotificationBanner.startAnimation(anim);
                }
            }, 4000);
        }
    }
}
