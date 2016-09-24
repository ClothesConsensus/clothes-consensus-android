package com.fadetoproductions.rvkn.clothesconsensus.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.fadetoproductions.rvkn.clothesconsensus.R;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;
    boolean swipeRight;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
//        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onSwipeRight(viewHolder.getAdapterPosition());
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition(), direction);
    }
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
                                  int actionState) {
        // We only want the active item
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof ItemTouchHelperViewHolder) {
                ItemTouchHelperViewHolder itemViewHolder =
                        (ItemTouchHelperViewHolder) viewHolder;
                itemViewHolder.onItemSelected();
            }
        }

        super.onSelectedChanged(viewHolder, actionState);
    }
    @Override
    public void clearView(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        View itemView = viewHolder.itemView;
        View likeView = itemView.findViewById(R.id.ivLike);
        View nopeView = itemView.findViewById(R.id.ivNope);
        likeView.setBackgroundResource(0);
        nopeView.setBackgroundResource(0);
        if (viewHolder instanceof ItemTouchHelperViewHolder) {
            ItemTouchHelperViewHolder itemViewHolder =
                    (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemClear();
        }
    }
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // Get RecyclerView item from the ViewHolder
            View itemView = viewHolder.itemView;
            View likeView = itemView.findViewById(R.id.ivLike);
            View nopeView = itemView.findViewById(R.id.ivNope);

            int position = viewHolder.getAdapterPosition();

            Paint p = new Paint();
            //TODO some bug in the following animation. Failing with : ```Fatal signal 11 (SIGSEGV), code 1, fault addr 0x5c in tid 32718```
            // ask.
//            int colorFrom=0;
//            if(dX > 0) {
//                colorFrom = recyclerView.getResources().getColor(R.color.lightGreen);
//
//            } else if(dX < 0){
//                colorFrom = recyclerView.getResources().getColor(R.color.lightRed);
//
//            }
//            int colorTo = recyclerView.getResources().getColor(R.color.white);
//            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
//            colorAnimation.setDuration(5000); // milliseconds
//            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//
//                @Override
//                public void onAnimationUpdate(ValueAnimator animator) {
//                    itemView.setBackgroundColor((int) animator.getAnimatedValue());
//                    if (dX > 0) {
//            /* Set your color for positive displacement */
////                        p.setColor((int) animator.getAnimatedValue());
//                        // Draw Rect with varying right side, equal to displacement dX
////                        c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
////                                (float) itemView.getBottom(), p);
//                        swipeRight = true;
//                        nopeView.setVisibility(View.GONE);
//                        nopeView.setBackgroundResource(0);
//                        likeView.setBackgroundResource(0);
//                        likeView.setBackgroundResource(R.mipmap.like);
//                        likeView.setVisibility(View.VISIBLE);
//                    } else if(dX < 0){
//            /* Set your color for negative displacement */
////                        p.setColor((int) animator.getAnimatedValue());
//                        // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
////                        c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
////                                (float) itemView.getRight(), (float) itemView.getBottom(), p);
////                mAdapter.onSwipeLeft(position);
//                        swipeRight = false;
//                        likeView.setVisibility(View.GONE);
//                        likeView.setBackgroundResource(0);
//                        nopeView.setBackgroundResource(0);
//                        nopeView.setBackgroundResource(R.mipmap.nope);
//                        nopeView.setVisibility(View.VISIBLE);
//                    }
//                }
//
//            });
//            colorAnimation.start();
            if (dX > 0) {
            /* Set your color for positive displacement */
                p.setColor(recyclerView.getResources().getColor(R.color.lightGreen));
                // Draw Rect with varying right side, equal to displacement dX
                c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                        (float) itemView.getBottom(), p);
                swipeRight = true;
                nopeView.setVisibility(View.GONE);
                nopeView.setBackgroundResource(0);
                likeView.setBackgroundResource(0);
                likeView.setBackgroundResource(R.mipmap.like);
                likeView.setVisibility(View.VISIBLE);
            } else if(dX < 0){
            /* Set your color for negative displacement */
                p.setColor(recyclerView.getResources().getColor(R.color.lightRed));
                // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
                c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                        (float) itemView.getRight(), (float) itemView.getBottom(), p);
//                mAdapter.onSwipeLeft(position);
                swipeRight = false;
                likeView.setVisibility(View.GONE);
                likeView.setBackgroundResource(0);
                nopeView.setBackgroundResource(0);
                nopeView.setBackgroundResource(R.mipmap.nope);
                nopeView.setVisibility(View.VISIBLE);
            }


            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }


}
