package com.fadetoproductions.rvkn.clothesconsensus.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.activity.BaseActivity;
import com.fadetoproductions.rvkn.clothesconsensus.fragments.ProfileLookDialogueFragment;
import com.fadetoproductions.rvkn.clothesconsensus.models.Look;
import com.fadetoproductions.rvkn.clothesconsensus.utils.TimeUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;

/**
 * Created by rnewton on 9/18/16.
 */
public class ProfilesV2Adapter extends RecyclerView.Adapter<ProfilesV2Adapter.ViewHolder> {

    private static final int CURRENT_LOOK = 0;
    private static final int EXPIRED_LOOK = 1;


    private ArrayList<Look> mLooks;
    private BaseActivity mContext;

    public ProfilesV2Adapter(BaseActivity context, ArrayList<Look> looks) {
        mContext = context;
        mLooks = looks;
    }

//    @Override
//    public FinishedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View lookView = LayoutInflater.from(mContext).inflate(R.layout.item_look_result, parent, false);
//        return new FinishedViewHolder(lookView);
//    }

//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
//        Look look = mLooks.get(position);
////        viewHolder.loadFrom(look);
//    }

    @Override
    public int getItemCount() {
        return mLooks.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View lookView = LayoutInflater.from(mContext).inflate(R.layout.item_look_result, parent, false);
        View lookView = null;

        switch (viewType){
            case EXPIRED_LOOK :
                lookView = LayoutInflater.from(mContext).inflate(R.layout.item_look_result, parent, false);
                break;
            case CURRENT_LOOK:
                lookView = LayoutInflater.from(mContext).inflate(R.layout.item_look_currently_live, parent, false);
                break;
        }

        return new ViewHolder(lookView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Look look = mLooks.get(position);
        viewHolder.loadFrom(look);
    }

    @Override
    public int getItemViewType(int position) {
        Look look = mLooks.get(position);
        if (look.isExpired())
            return EXPIRED_LOOK;
        else
            return  CURRENT_LOOK;
    }

    public static class ViewHolder extends AnimateViewHolder {

        BaseActivity mContext;
        Look look;
        @BindView(R.id.tvDislikes) TextView tvDislikes;
        @Nullable @BindView(R.id.tvLikes) TextView tvLikes;
        @Nullable @BindView(R.id.ivLookImage) ImageView ivLookImage;
        @Nullable @BindView(R.id.tvTimeRemaining) TextView tvTimeRemaining;

        public ViewHolder(final View itemView) {
            super(itemView);
            mContext = (BaseActivity) itemView.getContext();
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fm = mContext.getSupportFragmentManager();
                    ProfileLookDialogueFragment lookDialogueFragment = ProfileLookDialogueFragment.newInstance(look);
                    lookDialogueFragment.show(fm, "fragment_edit_name");
                }
            });
        }

        @Override
        public void animateAddImpl(ViewPropertyAnimatorListener listener) {
            ViewCompat.setTranslationY(itemView, -itemView.getHeight() * 0.3f);
            ViewCompat.setAlpha(itemView, 0);
        }

        @Override
        public void animateRemoveImpl(ViewPropertyAnimatorListener listener) {
            ViewCompat.animate(itemView)
                    .translationY(-itemView.getHeight() * 0.3f)
                    .alpha(0)
                    .setDuration(300)
                    .setListener(listener)
                    .start();
        }

        @Override
        public void preAnimateAddImpl() {
            ViewCompat.setTranslationY(itemView, -itemView.getHeight() * 0.3f);
            ViewCompat.setAlpha(itemView, 0);
        }




        public void loadFrom(Look look) {
            this.look = look;

            if (this.look.isExpired()) {
                tvLikes.setText(Integer.toString(look.getVotesYes()));
                tvDislikes.setText(Integer.toString(look.getVotesNo()));
            } else {
                String verboseTimeString = TimeUtils.minutesToVerboseString((int) look.getMinutesRemaining());
                tvTimeRemaining.setText(verboseTimeString + " \n remaining");
            }
            ivLookImage.setImageResource(0);
            Picasso.with(mContext).load(look.getPhotoUrl()).into(ivLookImage);
        }
    }
}
