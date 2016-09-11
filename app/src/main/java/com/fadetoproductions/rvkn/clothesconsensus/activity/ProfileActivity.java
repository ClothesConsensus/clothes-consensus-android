package com.fadetoproductions.rvkn.clothesconsensus.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.adapter.ProfilesAdapter;
import com.fadetoproductions.rvkn.clothesconsensus.databinding.ActivityProfileBinding;
import com.fadetoproductions.rvkn.clothesconsensus.databinding.ToolbarBinding;
import com.fadetoproductions.rvkn.clothesconsensus.models.Look;
import com.fadetoproductions.rvkn.clothesconsensus.models.User;
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
//        User user = Parcels.unwrap(getIntent().getParcelableExtra("user"));

//        ButterKnife.bind(this);

//        populateProfileHeader(user);
        RecyclerView rvProfile = activityProfileBinding.rvProfile;
        adapter = new ProfilesAdapter(this,profileLooks);
        rvProfile.setAdapter(adapter);
        rvProfile.setLayoutManager(new LinearLayoutManager(this));

        //Network call here to fetch the looks.
        //Make the model
        //
        client.getLooks();




    }
    private void populateProfileHeader(User user) {
        final String screenName = user.getName();
        Picasso.with(this).load(user.getProfileImageUrl()).
                transform(new RoundedCornersTransformation(2,2)).into(activityProfileBinding.ivProfileImage);
        activityProfileBinding.tvName.setText(user.getName());
    }


    public void onGetLooks(ArrayList<Look> fetchedLooks) {
        profileLooks.addAll(fetchedLooks);
        Log.v("action", "Looks fetched");
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.ibCamera)
    public void loadCamera() {
        super.loadCamera();
    }

}
