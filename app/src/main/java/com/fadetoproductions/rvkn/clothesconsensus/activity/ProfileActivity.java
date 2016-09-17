package com.fadetoproductions.rvkn.clothesconsensus.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.adapter.ProfilesAdapter;
import com.fadetoproductions.rvkn.clothesconsensus.databinding.ActivityProfileBinding;
import com.fadetoproductions.rvkn.clothesconsensus.models.Look;
import com.fadetoproductions.rvkn.clothesconsensus.models.User;
import com.fadetoproductions.rvkn.clothesconsensus.utils.DividerItemDecoration;
import com.squareup.picasso.Picasso;

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
        adapter = new ProfilesAdapter(this,profileLooks);
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
