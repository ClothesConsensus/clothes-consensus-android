package com.fadetoproductions.rvkn.clothesconsensus.fragments;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.models.Look;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rnewton on 9/18/16.
 */
public class ProfileLookDialogueFragment extends DialogFragment {

    Look look;

    @BindView(R.id.ivLookImage) ImageView ivLookImage;

    public ProfileLookDialogueFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ProfileLookDialogueFragment newInstance(Look look) {
        ProfileLookDialogueFragment frag = new ProfileLookDialogueFragment();
        Bundle args = new Bundle();
        args.putParcelable("look", Parcels.wrap(look));
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.fragment_profile_look, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        look = Parcels.unwrap(getArguments().getParcelable("look"));
        Picasso.with(this.getContext()).load(look.getPhotoUrl()).into(ivLookImage);
        ivLookImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ivLookImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ivLookImage.getLayoutParams().height = ivLookImage.getWidth();
            }
        });
    }

    @Override
    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout(size.x, size.y);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }

    @OnClick(R.id.rlFragmentContainer)
    public void closeFragment(View view) {
        dismiss();
    }
}
