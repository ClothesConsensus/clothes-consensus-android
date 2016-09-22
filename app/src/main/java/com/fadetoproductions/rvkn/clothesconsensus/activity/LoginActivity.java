package com.fadetoproductions.rvkn.clothesconsensus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.rlEmailLogin) RelativeLayout rlEmailLogin;
    @BindView(R.id.rlButtonContainer) RelativeLayout rlButtonContainer;
    @BindView(R.id.etEmail) EditText etEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User user = User.getLoggedInUser(this);
        if (user != null) {
            startHomeActivity();
        }

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.rlButtonContainer)
    public void hideLoginWithEmailStuff(View view) {
        if (rlEmailLogin.getVisibility() != View.VISIBLE) {
            return;
        }

        Animation slideAnim = AnimationUtils.loadAnimation(this, R.anim.slide_down_email);
        slideAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rlEmailLogin.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        rlEmailLogin.startAnimation(slideAnim);
    }


    @OnClick(R.id.btnSignInWithEmail)
    public void loginWithEmail(View view) {
        stopProgressBar();

        rlEmailLogin.setVisibility(View.VISIBLE);

        Animation slideAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up_email);
        slideAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Set cursor to the end of edittext
                Selection.setSelection(etEmail.getText(), etEmail.getText().length());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        rlEmailLogin.startAnimation(slideAnim);
    }

    @OnClick({R.id.btnSignInWithTwitter, R.id.btnSignInWithFacebook, R.id.btnCompleteLogin})
    public void login(View view) {
        Log.v("actions", "Logging in");
        //TODO: Hardcoded user for now. Change to the user which is logged in.
        client.getUser(145);
        startProgressBar();
    }

    @Override
    public void onGetUser(User user) {
        super.onGetUser(user);
        User.setLoggedInUser(this, user);
        startHomeActivity();
    }

    private void startHomeActivity() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }
}
