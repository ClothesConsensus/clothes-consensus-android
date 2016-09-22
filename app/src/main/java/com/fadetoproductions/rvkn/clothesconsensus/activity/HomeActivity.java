package com.fadetoproductions.rvkn.clothesconsensus.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.adapter.LooksAdapter;
import com.fadetoproductions.rvkn.clothesconsensus.databinding.ActivityHomeBinding;
import com.fadetoproductions.rvkn.clothesconsensus.models.Look;
import com.fadetoproductions.rvkn.clothesconsensus.models.User;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements LooksAdapter.LookVoteListener {

    ActivityHomeBinding activityHomeBinding;
    LooksAdapter adapter;
    ArrayList<Look> looks;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        looks = new ArrayList<>();
        adapter = new LooksAdapter(this, looks);
        adapter.lookVoteListener = this;

        RecyclerView rvLooks = activityHomeBinding.rvLooks;
        rvLooks.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rvLooks.setLayoutManager(linearLayoutManager);

        user = User.getLoggedInUser(this);

        ButterKnife.bind(this);
        client.getLooks();
        startProgressBar();
    }

    public void onGetLooks(ArrayList<Look> fetchedLooks) {
        super.onGetLooks(fetchedLooks);
        looks.addAll(fetchedLooks);
        Log.v("action", "Looks fetched");
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.ibProfile)
    public void loadProfile(View view) {
        super.loadProfileForUser(user);
    }

    @OnClick(R.id.ibCamera)
    public void loadCamera() {
        super.loadCamera();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onVoteLook(Look look, Boolean vote) {
        client.postVoteForLook(user.getUserId(), look.getLookId(), vote);
    }
}
