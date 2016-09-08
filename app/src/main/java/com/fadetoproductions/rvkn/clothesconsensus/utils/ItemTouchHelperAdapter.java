package com.fadetoproductions.rvkn.clothesconsensus.utils;

/**
 * Created by sdass on 9/6/16.
 */
public interface ItemTouchHelperAdapter {

    void onItemDismiss(int position, int direction);

    void onSwipeRight(int position);

    void onSwipeLeft(int position);
}
