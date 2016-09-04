package com.fadetoproductions.rvkn.clothesconsensus.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.utils.PhotoUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LookConfirmationActivity extends AppCompatActivity {


    Bitmap lookImage;
    @BindView(R.id.ivLookImage) ImageView ivLookImage;

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
}
