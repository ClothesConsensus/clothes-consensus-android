package com.fadetoproductions.rvkn.clothesconsensus.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.adapter.LooksAdapter;
import com.fadetoproductions.rvkn.clothesconsensus.databinding.ActivityHomeBinding;
import com.fadetoproductions.rvkn.clothesconsensus.models.Look;
import com.fadetoproductions.rvkn.clothesconsensus.models.User;
import com.fadetoproductions.rvkn.clothesconsensus.utils.DividerItemDecoration;
import com.fadetoproductions.rvkn.clothesconsensus.utils.SimpleItemTouchHelperCallback;

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
        rvLooks.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        rvLooks.addItemDecoration(itemDecoration);
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rvLooks);
        user = User.getLoggedInUser(this);

        ButterKnife.bind(this);
        client.getLooks();
    }

    public void onGetLooks(ArrayList<Look> fetchedLooks) {
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
