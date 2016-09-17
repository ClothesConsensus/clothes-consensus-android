package com.fadetoproductions.rvkn.clothesconsensus.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.databinding.CurrentProfileRowBinding;
import com.fadetoproductions.rvkn.clothesconsensus.databinding.ExpiredProfileRowBinding;
import com.fadetoproductions.rvkn.clothesconsensus.models.Look;
import com.fadetoproductions.rvkn.clothesconsensus.models.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by sdass on 9/10/16.
 */
public class ProfilesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int CURRENT_LOOK = 0;
    private static final int EXPIRED_LOOK = 1;

    public static class ExpiredProfileViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivLook;
        public TextView tvRatings;
        public TextView tvMessage;
        public ImageView ivBackgroundImage;
        ExpiredProfileRowBinding binding;
        public ExpiredProfileViewHolder(View itemView, ExpiredProfileRowBinding binding) {
            super(itemView);
            this.binding = binding;
            ivLook = binding.ivExpiredLook;
            tvRatings = binding.tvRating;
            tvMessage = binding.tvMessage;
            ivBackgroundImage = binding.ivBackgroundImage;

        }
    }

    public static class CurrentProfileViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivLook;
        public TextView tvRatings;
        public TextView tvMessage;
        CurrentProfileRowBinding binding;
        public CurrentProfileViewHolder(View itemView, CurrentProfileRowBinding binding) {
            super(itemView);
            this.binding = binding;
            ivLook = binding.ivExpiredLook;
            tvRatings = binding.tvRating;
            tvMessage = binding.tvMessage;
        }
    }

    private ArrayList<Look> mLooks;
    private Context mContext;

    public ProfilesAdapter(Context context, ArrayList<Look> looks) {
        mContext  = context;
        mLooks = looks;
    }
    private Context getContext() {
        return mContext;
    }

    @Override
    public int getItemViewType(int position) {
        Look look = mLooks.get(position);
        if (look.isCurrent())
            return CURRENT_LOOK;
        else
            return  EXPIRED_LOOK;
//        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder viewHolder = null;

        // Inflate the custom layout
        switch (viewType){
            case EXPIRED_LOOK :
                ExpiredProfileRowBinding binding= DataBindingUtil.inflate(inflater,
                        R.layout.expired_profile_row, parent, false);
                viewHolder = new ExpiredProfileViewHolder(binding.getRoot(), binding);
                break;
            case CURRENT_LOOK:
                CurrentProfileRowBinding binding2= DataBindingUtil.inflate(inflater,
                        R.layout.current_profile_row, parent, false);
                viewHolder = new CurrentProfileViewHolder(binding2.getRoot(), binding2);
                break;
        }

        // Return a new holder instance

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        // Set item views based on your views and data model
        switch (viewHolder.getItemViewType()) {
            case EXPIRED_LOOK:
                ExpiredProfileViewHolder vh1 = (ExpiredProfileViewHolder) viewHolder;
                configureExpireViewHolder(vh1, position);
                break;
            case CURRENT_LOOK:
                CurrentProfileViewHolder vh2 = (CurrentProfileViewHolder) viewHolder;
                configureCurrentViewHolder(vh2, position);
                break;
        }

    }

    private void configureExpireViewHolder(ExpiredProfileViewHolder viewHolder, int position) {
        Look look = mLooks.get(position);

        final TextView tvRatings = viewHolder.tvRatings;
        final TextView tvMessage = viewHolder.tvMessage;
        final ImageView ivBackground = viewHolder.ivBackgroundImage;
        final ImageView ivLook = viewHolder.ivLook;

        Target target = new Target() {
            // Fires when Picasso finishes loading the bitmap for the target
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // TODO 1. Insert the bitmap into the profile image view
                ivLook.setImageBitmap(bitmap);
                // TODO 2. Use generate() method from the Palette API to get the vibrant color from the bitmap
                // Set the result as the background color for `R.id.vPalette` view containing the contact's name.
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        Palette.Swatch swatch = palette.getVibrantSwatch();
                        if(swatch != null) {
                            ivBackground.setBackgroundColor(swatch.getRgb());
                            tvMessage.setTextColor(swatch.getTitleTextColor());
                            tvRatings.setTextColor(swatch.getTitleTextColor());
                        }
                    }
                });
            }

            // Fires if bitmap fails to load
            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };        //TODO : Ideally, we will be doing the calculation of rating on backend.
        tvRatings.setText(""+look.findAverageRating());
        Picasso.with(getContext()).load(look.getPhotoUrl()).
                transform(new RoundedCornersTransformation(5,5)).resize(310,310).into(target);
        tvMessage.setText(look.getMessage());

    }

    private void configureCurrentViewHolder(CurrentProfileViewHolder viewHolder, int position) {
        Look look = mLooks.get(position);
        TextView textView = viewHolder.tvRatings;
        TextView tvMessage = viewHolder.tvMessage;
        //TODO : Ideally, we will be doing the calculation of rating on backend.
        textView.setText(""+look.findAverageRating());
        ImageView imageView = viewHolder.ivLook;
        Picasso.with(getContext()).load(look.getPhotoUrl()).
                transform(new RoundedCornersTransformation(5,5)).resize(310,310).into(imageView);
        tvMessage.setText(look.getMessage());
    }
    @Override
    public int getItemCount() {
        return mLooks.size();
    }
}
