package com.fadetoproductions.rvkn.clothesconsensus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.fadetoproductions.rvkn.clothesconsensus.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btnSignInWithTwitter, R.id.btnSignInWithEmail, R.id.btnSignInWithFacebook})
    public void login(View view) {
        Log.v("actions", "Logging in");
        Intent i = new Intent(this, HomeActivity.class);
        //TODO: Hardcoded user for now. Change to the user which is logged in.
        i.putExtra("user_id","22");
        startActivity(i);
    }

}
