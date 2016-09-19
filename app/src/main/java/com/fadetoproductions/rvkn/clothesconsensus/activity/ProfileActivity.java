package com.fadetoproductions.rvkn.clothesconsensus.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.adapter.ProfilesAdapter;
import com.fadetoproductions.rvkn.clothesconsensus.databinding.ActivityProfileBinding;
import com.fadetoproductions.rvkn.clothesconsensus.models.Look;
import com.fadetoproductions.rvkn.clothesconsensus.models.User;
import com.fadetoproductions.rvkn.clothesconsensus.utils.DividerItemDecoration;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ProfileActivity extends BaseActivity {
    ActivityProfileBinding activityProfileBinding;
    ArrayList<Look> profileLooks;
    ProfilesAdapter adapter;
    User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        profileLooks = new ArrayList<>();

        //TODO We need to find the logged in user here.
        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));
        populateProfileHeader();
        RecyclerView rvProfile = activityProfileBinding.rvProfile;
        final ImageView ivBackground = activityProfileBinding.ivBackgroundImage;
        Target target = new Target() {
            // Fires when Picasso finishes loading the bitmap for the target
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // TODO 1. Insert the bitmap into the profile image view
                ivBackground.setImageBitmap(bitmap);
                // TODO 2. Use generate() method from the Palette API to get the vibrant color from the bitmap
                // Set the result as the background color for `R.id.vPalette` view containing the contact's name.
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        Palette.Swatch swatch = palette.getVibrantSwatch();
                        if(swatch != null) {
                            activityProfileBinding.tvName.setTextColor(swatch.getTitleTextColor());
                        }
                    }
                });
            }

            // Fires if bitmap fails to load
            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(this).load(user.getBannerImageUrl()).into(target);
        adapter = new ProfilesAdapter(this, profileLooks);
        rvProfile.setAdapter(adapter);
        rvProfile.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        rvProfile.addItemDecoration(itemDecoration);

        client.getUser(user.getUserId());

        ButterKnife.bind(this);
    }
    private void populateProfileHeader() {
        Picasso.with(this).load(user.getProfileImageUrl()).
                transform(new RoundedCornersTransformation(5, 5)).resize(75, 75).into(activityProfileBinding.ivProfileImage);
        activityProfileBinding.tvName.setText(user.getName());
    }

    @OnClick(R.id.ibCamera)
    public void loadCamera() {
        super.loadCamera();
    }

    @OnClick(R.id.ibBack)
    public void back() {
        super.back();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onGetUser(User user) {
        profileLooks.addAll(user.getLooks());
        adapter.notifyDataSetChanged();
        this.user = user;
    }
}
