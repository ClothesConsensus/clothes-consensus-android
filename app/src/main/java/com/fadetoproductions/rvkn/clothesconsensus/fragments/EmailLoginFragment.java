package com.fadetoproductions.rvkn.clothesconsensus.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.fadetoproductions.rvkn.clothesconsensus.R;

/**
 * Created by rnewton on 9/21/16.
 */
public class EmailLoginFragment extends DialogFragment {
    public EmailLoginFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EmailLoginFragment newInstance() {
        EmailLoginFragment frag = new EmailLoginFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.fragment_email_login, container);
    }
}
