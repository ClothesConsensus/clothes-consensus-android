package com.fadetoproductions.rvkn.clothesconsensus.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.adapter.LooksAdapter;
import com.fadetoproductions.rvkn.clothesconsensus.databinding.ActivityHomeBinding;
import com.fadetoproductions.rvkn.clothesconsensus.databinding.ToolbarBinding;
import com.fadetoproductions.rvkn.clothesconsensus.models.Look;
import com.fadetoproductions.rvkn.clothesconsensus.models.User;
import com.fadetoproductions.rvkn.clothesconsensus.utils.DividerItemDecoration;
import com.fadetoproductions.rvkn.clothesconsensus.utils.SimpleItemTouchHelperCallback;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements TimePickerDialog.OnTimeSetListener {

    ActivityHomeBinding activityHomeBinding;
    LooksAdapter adapter;
    ArrayList<Look> looks;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
//        setupActionBar();
        looks = new ArrayList<>();
        adapter = new LooksAdapter(this, looks);

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

    private void setupActionBar() {
        ToolbarBinding toolbarBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.toolbar, null, false);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbarBinding.llToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.logo);

        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.background));


        actionBar.setDisplayUseLogoEnabled(true);


    }
    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        // Do something with the tvTime here.

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
}
