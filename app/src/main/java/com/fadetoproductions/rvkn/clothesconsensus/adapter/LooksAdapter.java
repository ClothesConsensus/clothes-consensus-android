package com.fadetoproductions.rvkn.clothesconsensus.adapter;

/**
 * Created by sdass on 8/31/16.
 */

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.activity.BaseActivity;
import com.fadetoproductions.rvkn.clothesconsensus.databinding.LookCardBinding;
import com.fadetoproductions.rvkn.clothesconsensus.models.Look;
import com.fadetoproductions.rvkn.clothesconsensus.utils.ItemTouchHelperAdapter;
import com.fadetoproductions.rvkn.clothesconsensus.utils.ItemTouchHelperViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class LooksAdapter extends RecyclerView.Adapter<LooksAdapter.LookViewHolder> implements ItemTouchHelperAdapter {

    private BaseActivity mContext;

    private List<Look> mLooks;

    private BaseActivity getContext() {
        return mContext;
    }

    public LookVoteListener lookVoteListener;

    public LooksAdapter(BaseActivity mContext, List<Look> looks) {
        this.mContext = mContext;
        this.mLooks = looks;
    }

    public interface LookVoteListener {
        void onVoteLook(Look look, Boolean vote);
    }

    @Override
    public LooksAdapter.LookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LookCardBinding lookCardBinding = DataBindingUtil.inflate(inflater, R.layout.look_card, parent, false);
        LookViewHolder viewHolder = new LookViewHolder(getContext(), lookCardBinding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LooksAdapter.LookViewHolder viewHolder, int position) {
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

    @Override
    public void onItemDismiss(int position, int direction) {
        mLooks.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onSwipeRight(int position) {
        Look look = mLooks.get(position);
        if (lookVoteListener != null) {
            lookVoteListener.onVoteLook(look, true);
        }
    }

    @Override
    public void onSwipeLeft(int position) {
        Look look = mLooks.get(position);
        if (lookVoteListener != null) {
            lookVoteListener.onVoteLook(look, false);
        }
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class LookViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        final LookCardBinding lookCardBinding;
        private BaseActivity mContext;
        Look look;
        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public LookViewHolder(BaseActivity context, LookCardBinding binding) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(binding.getRoot());
            this.lookCardBinding = binding;
            this.mContext = context;
        }

        public void bindLook(final Look look){
            this.look = look;
            ImageView thumbnail = lookCardBinding.rlCaption.ivThumbnail;
            EditText message = lookCardBinding.rlCaption.etMessage;
            message.setText(look.getMessage());
            ImageView ivLook = lookCardBinding.ivLookImage;
            thumbnail.setImageResource(0);
            ivLook.setImageResource(0);
            Picasso.with(mContext).load(look.getUser().getProfileImageUrl()).transform(new RoundedCornersTransformation(5,0)).fit().into(thumbnail);
            Picasso.with(mContext).load(look.getPhotoUrl()).transform(new RoundedCornersTransformation(10,0, RoundedCornersTransformation.CornerType.ALL)).fit().into(ivLook);

            // TODO This shouldn't be done this way
            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.loadProfileForUser(look.getUser());
                }
            });
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
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
