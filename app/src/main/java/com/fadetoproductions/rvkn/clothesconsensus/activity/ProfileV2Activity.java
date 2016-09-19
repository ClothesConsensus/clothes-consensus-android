package com.fadetoproductions.rvkn.clothesconsensus.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.adapter.ProfilesV2Adapter;
import com.fadetoproductions.rvkn.clothesconsensus.models.Look;
import com.fadetoproductions.rvkn.clothesconsensus.models.User;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ProfileV2Activity extends BaseActivity {

    private SwipeRefreshLayout swipeContainer;
    ArrayList<Look> profileLooks;
    ProfilesV2Adapter adapter;
    User user;

    @BindView(R.id.rvProfile) RecyclerView rvProfile;
    @BindView(R.id.ivBackgroundImage) ImageView ivBackgroundImage;
    @BindView(R.id.ivThumbnail) ImageView ivThumbnail;
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvLookCount) TextView tvLookCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_v2);
        ButterKnife.bind(this);

        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));
        profileLooks = new ArrayList<>();
        populateProfileHeader();


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvProfile.setLayoutManager(layoutManager);


        adapter = new ProfilesV2Adapter(this, profileLooks);
        rvProfile.setAdapter(adapter);


        client.getUser(user.getUserId());
    }

    private void populateProfileHeader() {
        Picasso.with(this).load(user.getBannerImageUrl()).into(ivBackgroundImage);
        Picasso.with(this).load(user.getProfileImageUrl()).transform(new RoundedCornersTransformation(5, 5)).resize(75, 75).into(ivThumbnail);
        tvName.setText(user.getName());
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
        this.user = user;
        profileLooks.addAll(user.getLooks());
        adapter.notifyDataSetChanged();
        tvLookCount.setText(Integer.toString(profileLooks.size()) + " looks");
    }
}
