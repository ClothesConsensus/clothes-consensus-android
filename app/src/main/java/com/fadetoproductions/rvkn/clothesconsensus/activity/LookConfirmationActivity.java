package com.fadetoproductions.rvkn.clothesconsensus.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.utils.PhotoUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LookConfirmationActivity extends BaseActivity {

    Bitmap lookImage;
    @BindView(R.id.ivLookImage) ImageView ivLookImage;
    @BindView(R.id.btnPostLook) Button btnPostLook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_confirmation);
        ButterKnife.bind(this);
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
        client.postLook(lookImage);
    }

    @Override
    public void onPostLook(JSONObject response) {
        finish();
    }
}