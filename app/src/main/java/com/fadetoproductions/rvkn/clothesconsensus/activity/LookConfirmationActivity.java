package com.fadetoproductions.rvkn.clothesconsensus.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.models.User;
import com.fadetoproductions.rvkn.clothesconsensus.utils.PhotoUtils;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LookConfirmationActivity extends BaseActivity {

    Bitmap lookImage;
    User user;

    @BindView(R.id.ivLookImage) ImageView ivLookImage;
    @BindView(R.id.btnPostLook) Button btnPostLook;
    @BindView(R.id.etMessage) EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_confirmation);
        ButterKnife.bind(this);
        user = User.getLoggedInUser(this);
        loadLookImage();
    }

    private void loadLookImage() {
        Uri takenPhotoUri = PhotoUtils.getPhotoFileUri(this, PhotoUtils.PHOTO_FILE_NAME);
        lookImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
        ivLookImage.setImageBitmap(lookImage);
    }

    @OnClick(R.id.btnPostLook)
    public void postLook(View view) {
        // Some post logic here

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        TimeZone tz = TimeZone.getTimeZone("UTC");
        df.setTimeZone(tz);
        String expirationString = df.format(new Date());

        String message = etMessage.getText().toString();
        String imageString = PhotoUtils.encodeBitmapToSendableString(lookImage);


        client.postLook(user.getUserId(), message, expirationString, imageString);
    }

    @Override
    public void onPostLook(JSONObject response) {
        finish();
    }
}
