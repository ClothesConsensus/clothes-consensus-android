package com.fadetoproductions.rvkn.clothesconsensus.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.adapter.ProfilesAdapter;
import com.fadetoproductions.rvkn.clothesconsensus.databinding.ActivityProfileBinding;
import com.fadetoproductions.rvkn.clothesconsensus.databinding.ToolbarBinding;
import com.fadetoproductions.rvkn.clothesconsensus.models.Look;
import com.fadetoproductions.rvkn.clothesconsensus.models.User;
import com.fadetoproductions.rvkn.clothesconsensus.utils.DividerItemDecoration;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

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
        activityProfileBinding = DataBindingUtil.setContentView(this,R.layout.activity_profile);
        profileLooks = new ArrayList<>();
        ToolbarBinding toolbarBinding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.toolbar,null,false);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbarBinding.llToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        //TODO We need to find the logged in user here.
        user = User.getLoggedInUser(this);
        populateProfileHeader();
        RecyclerView rvProfile = activityProfileBinding.rvProfile;
        adapter = new ProfilesAdapter(this,profileLooks);
        rvProfile.setAdapter(adapter);
        rvProfile.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        rvProfile.addItemDecoration(itemDecoration);
        //Network call here to fetch the looks.
        //Make the model
        //TODO get the looks according to user logged in. Right now hardcoded to my id.
        client.getUser(""+user.getUserId());
    }
    private void populateProfileHeader() {
        Picasso.with(this).load(user.getProfileImageUrl()).
                transform(new RoundedCornersTransformation(5,5)).resize(75,75).into(activityProfileBinding.ivProfileImage);
        activityProfileBinding.tvName.setText(user.getName());
    }

    @OnClick(R.id.ibCamera)
    public void loadCamera() {
        super.loadCamera();
    }

    @Override
    public void onGetUser(User user) {
        profileLooks.addAll(user.getLooks());
        adapter.notifyDataSetChanged();
        this.user = user;
    }
}
