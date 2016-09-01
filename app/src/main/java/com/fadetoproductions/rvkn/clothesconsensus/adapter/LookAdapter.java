package com.fadetoproductions.rvkn.clothesconsensus.adapter;

/**
 * Created by sdass on 8/31/16.
 */

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.databinding.LookCardBinding;
import com.fadetoproductions.rvkn.clothesconsensus.models.Look;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class LookAdapter extends
        RecyclerView.Adapter<LookAdapter.LookViewHolder> {
    private Context mContext;
    private List<Look> mLooks;
    private Context getContext() {
        return mContext;
    }
    public LookAdapter(Context mContext, List<Look> looks) {
        this.mContext = mContext;
        this.mLooks = looks;
    }
    @Override
    public LookAdapter.LookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LookViewHolder viewHolder = null;
        LookCardBinding lookCardBinding = DataBindingUtil
                        .inflate(inflater, R.layout.look_card, parent, false);
        viewHolder = new LookViewHolder(getContext(), lookCardBinding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LookAdapter.LookViewHolder viewHolder, int position) {
        Look look = mLooks.get(position);

        LookViewHolder lvh = viewHolder;
        if (look != null) {
            lvh.bindLook(look);
        }
    }

    @Override
    public int getItemCount() {
        return mLooks.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class LookViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        final LookCardBinding lookCardBinding;
        private Context mContext;
        Look look;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public LookViewHolder(Context context, LookCardBinding binding) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(binding.getRoot());
            this.lookCardBinding = binding;
            this.mContext = context;

        }
        public void bindLook(final Look look){

            this.look = look;
            ImageView thumbnail = lookCardBinding.ivThumbnail;
            TextView message = lookCardBinding.tvMessage;
            ImageView ivLook = lookCardBinding.ivLook;
            RatingBar rb = lookCardBinding.rbVotes;
            thumbnail.setImageResource(0);
            Picasso.with(mContext).load(look.getThumbnailImage()).
                    transform(new RoundedCornersTransformation(2,2)).into(thumbnail);
            Picasso.with(mContext).load(look.getPhotoUrl())
                    .transform(new RoundedCornersTransformation(2,2)).into(ivLook);
            message.setText(look.getMessage());
            rb.setRating(look.findAverageRating());

        }
    }

    public void clear() {
        mLooks.clear();
        notifyDataSetChanged();
    }
    // Add a list of items
    public void addAll(List<Look> looks) {
        mLooks.addAll(0,looks);
        notifyDataSetChanged();
    }
}
