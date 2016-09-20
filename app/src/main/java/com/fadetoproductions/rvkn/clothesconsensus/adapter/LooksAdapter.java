package com.fadetoproductions.rvkn.clothesconsensus.adapter;

/**
 * Created by sdass on 8/31/16.
 */

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.fadetoproductions.rvkn.clothesconsensus.R;
import com.fadetoproductions.rvkn.clothesconsensus.activity.BaseActivity;
import com.fadetoproductions.rvkn.clothesconsensus.databinding.LookCardBinding;
import com.fadetoproductions.rvkn.clothesconsensus.models.Look;
import com.fadetoproductions.rvkn.clothesconsensus.utils.ItemTouchHelperViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


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

//    @Override
    public void onVoteLook(Look look, Boolean vote) {
        mLooks.remove(0);
        notifyItemRemoved(0);
        if (lookVoteListener != null) {
            lookVoteListener.onVoteLook(look, vote);
            Log.v("yyaaaay", "VOTED!!!!");
        }
    }

    //    public void animate(RecyclerView.ViewHolder viewHolder) {
//        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
//        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
//    }
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
//        animate(viewHolder);
        LookViewHolder lvh = viewHolder;
        if (look != null) {
            lvh.bindLook(this, look);

        }
    }

    @Override
    public int getItemCount() {
        return mLooks.size();
    }




//    @Override
//    public void onItemDismiss(int position, int direction) {
//        mLooks.remove(position);
//        notifyItemRemoved(position);
//    }
//
//    @Override
//    public void onSwipeRight(int position) {
//        Look look = mLooks.get(position);
//        if (lookVoteListener != null) {
//            lookVoteListener.onVoteLook(look, true);
//        }
//    }
//
//    @Override
//    public void onSwipeLeft(int position) {
//        Look look = mLooks.get(position);
//        if (lookVoteListener != null) {
//            lookVoteListener.onVoteLook(look, false);
//        }
//    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class LookViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

//        public interface ViewHolderLookVoteListener {
//            void onVoteLook(Look look, Boolean vote);
//        }

        final LookCardBinding lookCardBinding;
        private BaseActivity mContext;
        Look look;
        ImageView ivLook;
        ImageView ivSmile;
        ImageView ivUnsmile;
        View vDraggableZoneYes;
        View vDraggableZoneNo;
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
            message.setText(look.getMessage());
            vDraggableZoneNo = lookCardBinding.vDraggableZoneNo;
            vDraggableZoneYes = lookCardBinding.vDraggableZoneYes;
            ivSmile = lookCardBinding.ivSmile;
            ivUnsmile = lookCardBinding.ivUnsmile;
            ivLook = lookCardBinding.ivLookImage;
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
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        // Construct draggable shadow for view
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                        // Start the drag of the shadow
                        view.startDrag(null, shadowBuilder, view, 0);
                        // Hide the actual view as shadow is being dragged
                        view.setVisibility(View.INVISIBLE);
                        return true;
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        view.setVisibility(View.VISIBLE);
                        return false;
                    } else {
                        return false;
                    }
                }
            });

            vDraggableZoneYes.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    // Get the dragged view being dropped over a target view
                    final View draggedView = (View) event.getLocalState();
                    switch (event.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            // Signals the start of a drag and drop operation.
                            // Code for that event here
                            break;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            // Signals to a View that the drag point has
                            // entered the bounding box of the View.
                            Log.v("ACTION", "ENTERED");
                            ivSmile.setVisibility(View.VISIBLE);
                            break;
                        case DragEvent.ACTION_DRAG_EXITED:
                            // Signals that the user has moved the drag shadow
                            // outside the bounding box of the View.
                            Log.v("ACTION", "EXITED");
                            ivSmile.setVisibility(View.INVISIBLE);
                            break;
                        case DragEvent.ACTION_DROP:
                            ivLook.setVisibility(View.VISIBLE);
                            // Signals to a View that the user has released the drag shadow,
                            // and the drag point is within the bounding box of the View.
                            // Get View dragged item is being dropped on
                            View dropTarget = v;
                            // Make desired changes to the drop target below
                            dropTarget.setTag("dropped");
                            // Get owner of the dragged view and remove the view (if needed)
                            ViewGroup owner = (ViewGroup) draggedView.getParent();
                            owner.removeView(draggedView);
                            Log.v("ACTION", "DROPPED");
                            break;
                        case DragEvent.ACTION_DRAG_ENDED:
                            // Signals to a View that the drag and drop operation has concluded.
                            // If event result is set, this means the dragged view was dropped in target
                            ivLook.setVisibility(View.VISIBLE);
                            if (event.getResult()) { // drop succeeded
                                Log.v("ACTION", "ENDED INSIDE");
                                if (adapter != null) {
                                    ivLook.setVisibility(View.VISIBLE);
                                    adapter.onVoteLook(look, true);
                                }
                            } else { // drop did not occur
                                // restore the view as visible
                                draggedView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        draggedView.setVisibility(View.VISIBLE);
                                    }
                                });
                                // restore drop zone default background
                                Log.v("ACTION", "ENDED OUTSIDE");
                            }
                        default:
                            break;
                    }
                    ivLook.setVisibility(View.VISIBLE);
                    return true;
                }
            });

            vDraggableZoneNo.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    // Get the dragged view being dropped over a target view
                    final View draggedView = (View) event.getLocalState();
                    switch (event.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            // Signals the start of a drag and drop operation.
                            // Code for that event here
                            break;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            // Signals to a View that the drag point has
                            // entered the bounding box of the View.
                            Log.v("ACTION", "ENTERED");
                            ivUnsmile.setVisibility(View.VISIBLE);
                            break;
                        case DragEvent.ACTION_DRAG_EXITED:
                            // Signals that the user has moved the drag shadow
                            // outside the bounding box of the View.
                            Log.v("ACTION", "EXITED");
                            ivUnsmile.setVisibility(View.INVISIBLE);
                            break;
                        case DragEvent.ACTION_DROP:
                            ivLook.setVisibility(View.VISIBLE);
                            // Signals to a View that the user has released the drag shadow,
                            // and the drag point is within the bounding box of the View.
                            // Get View dragged item is being dropped on
                            View dropTarget = v;
                            // Make desired changes to the drop target below
                            dropTarget.setTag("dropped");
                            // Get owner of the dragged view and remove the view (if needed)
                            ViewGroup owner = (ViewGroup) draggedView.getParent();
                            owner.removeView(draggedView);
                            Log.v("ACTION", "DROPPED");
                            break;
                        case DragEvent.ACTION_DRAG_ENDED:
                            // Signals to a View that the drag and drop operation has concluded.
                            // If event result is set, this means the dragged view was dropped in target

                            if (event.getResult()) { // drop succeeded
                                Log.v("ACTION", "ENDED INSIDE");
                                if (adapter != null) {
                                    ivLook.setVisibility(View.VISIBLE);
                                    adapter.onVoteLook(look, false);
                                }
                            } else { // drop did not occur
                                // restore the view as visible
                                draggedView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        draggedView.setVisibility(View.VISIBLE);
                                    }
                                });
                                // restore drop zone default background
                                Log.v("ACTION", "ENDED OUTSIDE");
                            }
                        default:
                            break;
                    }
                    ivLook.setVisibility(View.VISIBLE);
                    return true;
                }
            });
        }
    }
}
