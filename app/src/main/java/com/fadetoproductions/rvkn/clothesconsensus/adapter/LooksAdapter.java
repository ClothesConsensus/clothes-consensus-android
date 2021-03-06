package com.fadetoproductions.rvkn.clothesconsensus.adapter;

/**
 * Created by sdass on 8/31/16.
 */

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.activity.BaseActivity;
import com.fadetoproductions.rvkn.clothesconsensus.databinding.LookCardBinding;
import com.fadetoproductions.rvkn.clothesconsensus.models.Look;
import com.fadetoproductions.rvkn.clothesconsensus.models.User;
import com.fadetoproductions.rvkn.clothesconsensus.utils.ItemTouchHelperViewHolder;
import com.fadetoproductions.rvkn.clothesconsensus.utils.TimeUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;


public class LooksAdapter extends RecyclerView.Adapter<LooksAdapter.LookViewHolder> {

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

    public void onVoteLook(Look look, Boolean vote) {
        mLooks.remove(0);
        notifyItemRemoved(0);
        if (lookVoteListener != null) {
            lookVoteListener.onVoteLook(look, vote);
            Log.v("yyaaaay", "VOTED!!!!");
        }
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
            lvh.bindLook(this, look);
        }
        cacheNextImage(position);
        cacheUserBackground(look.getUser());
    }

    public void cacheUserBackground(User user) {
        ImageView view = new ImageView(mContext);
        Picasso.with(mContext).load(user.getProfileImageUrl()).into(view);
    }

    public void cacheNextImage(int currentPosition) {
        // Load the upcoming image ahead of time
        int nextPosition = currentPosition + 2;
        if (mLooks.size() <= nextPosition) {
            return;
        }
        Look look = mLooks.get(nextPosition);
        ImageView view = new ImageView(mContext);
        Picasso.with(mContext).load(look.getPhotoUrl()).into(view);
    }

    @Override
    public int getItemCount() {
        return mLooks.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class LookViewHolder extends AnimateViewHolder implements ItemTouchHelperViewHolder {

        final LookCardBinding lookCardBinding;
        private BaseActivity mContext;
        float originalPostionX;
        float originalPositionY;
        float mLastTouchX;
        float mLastTouchY;
        private int mActivePointerId = 0;

        Look look;
        ImageView ivLook;
        ImageView ivSmile;
        ImageView ivUnsmile;
        TextView tvTimeRemaining;
        LooksAdapter adapter; // TODO this is really bad and hacky, but was having trouble with the listeners
//        ViewHolderLookVoteListener lookVoteListener;



        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public LookViewHolder(BaseActivity context, LookCardBinding binding) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(binding.getRoot());
            this.lookCardBinding = binding;
            this.mContext = context;
        }

        public void bindLook(LooksAdapter looksAdapter, final Look look){
            this.look = look;
            adapter = looksAdapter;
            ImageView thumbnail = lookCardBinding.rlCaption.ivThumbnail;
            EditText message = lookCardBinding.rlCaption.etMessage;
            message.setFocusable(false);
            message.setText(look.getMessage());
            ivSmile = lookCardBinding.ivSmile;
            ivUnsmile = lookCardBinding.ivUnsmile;
            ivLook = lookCardBinding.ivLookImage;
            ivLook.setVisibility(View.VISIBLE);
            tvTimeRemaining = lookCardBinding.tvTimeRemaining;
            tvTimeRemaining.setText(TimeUtils.minutesToSimpleString((int) look.getMinutesRemaining()));
            thumbnail.setImageResource(0);
            ivLook.setImageResource(0);
            Picasso.with(mContext)
                    .load(look.getUser().getProfileImageUrl())
                    .transform(new RoundedCornersTransformation(5,0))
                    .fit()
                    .into(thumbnail);
            Picasso.with(mContext).load(look.getPhotoUrl()).fit().into(ivLook);

            // TODO This shouldn't be done this way
            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.loadProfileForUser(look.getUser());
                }
            });

            setupDraggability();
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }

        public void setupDraggability() {
            ivLook.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(final View view, MotionEvent motionEvent) {
                    if (getPosition() != 0) {
                        // Prevent users from swiping cells that aren't the first
                        return false;
                    }

                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        final int pointerIndex = MotionEventCompat.getActionIndex(motionEvent);
                        final float x = MotionEventCompat.getX(motionEvent, pointerIndex);
                        final float y = MotionEventCompat.getY(motionEvent, pointerIndex);
                        mLastTouchX = x;
                        mLastTouchY = y;

                        originalPostionX = view.getX();
                        originalPositionY = view.getY();
                        return true;
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                        final int pointerIndex = MotionEventCompat.findPointerIndex(motionEvent, mActivePointerId);

                        final float x = MotionEventCompat.getX(motionEvent, pointerIndex);
                        final float y = MotionEventCompat.getY(motionEvent, pointerIndex);

                        // Calculate the distance moved
                        final float dx = x - mLastTouchX;
                        final float dy = y - mLastTouchY;

                        view.setY(view.getY() + dy);
                        view.setX(view.getX() + dx);

                        if (viewIsInLeftDropZone(view)) {
                            ivUnsmile.setVisibility(View.VISIBLE);
                            ivUnsmile.setImageAlpha(getAlphaForUnsmile(view));
                        } else if (viewIsInRightDropZone(view)) {
                            ivSmile.setVisibility(View.VISIBLE);
                            ivSmile.setImageAlpha(getAlphaForSmile(view));
                        } else {
                            ivUnsmile.setVisibility(View.INVISIBLE);
                            ivSmile.setVisibility(View.INVISIBLE);
                        }
                        return true;
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                        if (viewIsInLeftDropZone(view)) {
                            // Voted No
                            Log.v("s", "inside left");
                            adapter.onVoteLook(look, false);
                            view.setVisibility(View.INVISIBLE);
                            view.setY(originalPositionY);
                            view.setX(originalPostionX);
                        } else if (viewIsInRightDropZone(view)) {
                            // Voted Yes
                            Log.v("s", "inside right");
                            adapter.onVoteLook(look, true);
                            view.setVisibility(View.INVISIBLE);
                            view.setY(originalPositionY);
                            view.setX(originalPostionX);
                        } else {
                            // Didn't vote
                            TranslateAnimation anim = new TranslateAnimation(view.getX(), originalPostionX, view.getY(), originalPositionY);
                            anim.setDuration(150);
                            anim.setInterpolator(new AccelerateInterpolator());
                            anim.setAnimationListener(new TranslateAnimation.AnimationListener() {

                                @Override
                                public void onAnimationStart(Animation animation) { }

                                @Override
                                public void onAnimationRepeat(Animation animation) { }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    view.setY(originalPositionY);
                                    view.setX(originalPostionX);
                                }
                            });

                            view.startAnimation(anim);
                        }

                        ivUnsmile.setVisibility(View.INVISIBLE);
                        ivSmile.setVisibility(View.INVISIBLE);

                        return true;

                    } else {
                        return false;
                    }
                }
            });
        }
        public boolean viewIsInLeftDropZone(View view) {
            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
            float screenWidth = metrics.widthPixels;
            float percent = (float) 0.25;
            float leftBoundary = screenWidth * percent;
            float centerOfView = view.getX() + view.getWidth() / 2;
            return centerOfView < leftBoundary;
        }

        public boolean viewIsInRightDropZone(View view) {
            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
            float screenWidth = metrics.widthPixels;
            float percent = (float) 0.25;
            float rightBoundary = screenWidth - (screenWidth * percent);
            float centerOfView = view.getX() + view.getWidth() / 2;
            return centerOfView > rightBoundary;
        }

        public int getAlphaForSmile(View view) {
            // TODO simplify this...
            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
            float screenWidth = metrics.widthPixels;
            float percent = (float) 0.25;
            float rightBoundary = screenWidth - (screenWidth * percent);
            float centerOfView = view.getX() + view.getWidth() / 2;
            float amountBeyondBoundary = centerOfView - rightBoundary;
            float boundarySize = screenWidth * percent;
            float percentOfBoundaryCrossed = amountBeyondBoundary / boundarySize;
            float startingOpacity = (float) 0.5;
            // 255 is what setImageAlphaTakes
            int opacity = (int) (startingOpacity + percentOfBoundaryCrossed * 255);
            return Math.min(opacity, 255);
        }

        public int getAlphaForUnsmile(View view) {
            // TODO simplify this...
            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
            float screenWidth = metrics.widthPixels;
            float percent = (float) 0.25;
            float leftBoundary = screenWidth * percent;
            float centerOfView = view.getX() + view.getWidth() / 2;
            float amountBeyondBoundary = leftBoundary - centerOfView;
            float boundarySize = screenWidth * percent;
            float percentOfBoundaryCrossed = amountBeyondBoundary / boundarySize;
            float startingOpacity = (float) 0.5;
            // 255 is what setImageAlphaTakes
            int opacity = (int) (startingOpacity + percentOfBoundaryCrossed * 255);
            return Math.min(opacity, 255);
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
    }
}
