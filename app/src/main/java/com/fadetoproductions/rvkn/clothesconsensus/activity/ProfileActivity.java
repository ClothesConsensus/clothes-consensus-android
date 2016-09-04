package com.fadetoproductions.rvkn.clothesconsensus.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.databinding.ActivityProfileBinding;
import com.fadetoproductions.rvkn.clothesconsensus.databinding.ToolbarBinding;
import com.fadetoproductions.rvkn.clothesconsensus.models.User;

import org.parceler.Parcels;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding activityProfileBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBinding = DataBindingUtil.setContentView(this,R.layout.activity_profile);
        ToolbarBinding toolbarBinding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.toolbar,null,false);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbarBinding.llToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        User user = Parcels.unwrap(getIntent().getParcelableExtra("user"));
//        populateProfileHeader(user);
        //Network call here to fetch the looks.
        //Make the model
        //
//        if(savedInstanceState == null)
//            showYourLooksFragment(""+user.getUserId());
    }
//    private void populateProfileHeader(User user) {
//        final String screenName = user.getName();
//        Picasso.with(this).load(user.getProfileImageUrl()).
//                transform(new RoundedCornersTransformation(2,2)).into(activityProfileBinding.ivProfileImage);
//        activityProfileBinding.tvName.setText(user.getName());
//    }
//    private void showYourLooksFragment(String userId) {
//        YourLooksFragment yourLooksFragment = YourLooksFragment.newInstance(userId);
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(activityProfileBinding.flContainer.getId(), yourLooksFragment);
//        ft.commit();
//    }
}
