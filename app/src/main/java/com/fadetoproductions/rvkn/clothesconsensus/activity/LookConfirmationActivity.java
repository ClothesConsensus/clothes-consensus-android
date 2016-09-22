package com.fadetoproductions.rvkn.clothesconsensus.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.models.User;
import com.fadetoproductions.rvkn.clothesconsensus.utils.PhotoUtils;
import com.fadetoproductions.rvkn.clothesconsensus.utils.TimeUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LookConfirmationActivity extends BaseActivity {

    Bitmap lookImage;
    User user;

    int expirationTime = 120;
    @BindView(R.id.btnPostLook) Button btnPostLook;
    @BindView(R.id.etMessage) EditText etMessage;
    @BindView(R.id.ivLookImage) ImageView ivLookImage;
    @BindView(R.id.ivThumbnail) ImageView ivThumbnail;
    @BindView(R.id.rlTimePicker) RelativeLayout rlTimePicker;
    @BindView(R.id.rlClockImageAndText) RelativeLayout rlClockImageAndText;
    @BindView(R.id.tvTime) TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_confirmation);
        ButterKnife.bind(this);
        user = User.getLoggedInUser(this);
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivThumbnail);
        loadLookImage();
        setupTimeLister();
    }

    private void loadLookImage() {
        lookImage = PhotoUtils.resizeAndRotateAndGetCurrentImage(this);
        ivLookImage.setImageBitmap(lookImage);
    }

    private void setupTimeLister() {
        rlTimePicker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rlClockImageAndText.setY(event.getY());
                expirationTime = calculateTimeOnDatePicker(event.getY());
                tvTime.setText(TimeUtils.minutesToString(expirationTime));
                return true;
            }
        });
    }

    @OnClick(R.id.btnPostLook)
    public void postLook(View view) {
        Calendar calendar = Calendar.getInstance();
        Date expirationDate =new Date(calendar.getTimeInMillis() + (expirationTime * 60000));
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        TimeZone tz = TimeZone.getTimeZone("UTC");
        df.setTimeZone(tz);
        String expirationString = df.format(expirationDate);

        String message = etMessage.getText().toString();
        String imageString = PhotoUtils.encodeBitmapToSendableString(lookImage);
        client.postLook(user.getUserId(), message, expirationString, imageString);
        startProgressBar();
    }

    @OnClick(R.id.ibBackToCamera)
    public void backToCamera() {
        finish();
    }

    private float calculatePercentOnRlTimepicker(float eventY) {
        float height = rlTimePicker.getHeight();
        return eventY / height;
    }

    private int calculateTimeOnDatePicker(float eventY) {
        int timeMin = 5;  // In minutes
        int timeMax = 5 * 12 * 24; // half a day
        float percent = calculatePercentOnRlTimepicker(eventY);
        int expirationTimeInMinutes = (int) ((timeMax - timeMin) * percent + timeMin);

        if (expirationTimeInMinutes < timeMin) {
            // We shouldn't need to do this. Just some jankiness with calculations
            return timeMin;
        }
        return expirationTimeInMinutes;
    }

    @Override
    public void onPostLook(JSONObject response) {
        super.onPostLook(response);
        Intent data = new Intent();
        setResult(RESULT_OK, data);
        finish();
    }
}
