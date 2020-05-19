/**
 * Copyright (C) 2016 The Android Open Source Project
 * Developed by ssinss, downloaded from https://gist.github.com/ssinss/e06f12ef66c51252563e
 */

package popmovies.udacity.theMovieDBExampleMVP.view.controls;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Abstract implementation of endless on scroll listener that notifies
 * listener when user is about to reach the end of the list.
 */
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    /**
     * Total number of items in the dataset after the last load
     */
    protected int mPreviousTotal = 0;

    /**
     * True if we are still waiting for the last set of data to load.
     */
    protected boolean mLoading = true;

    /**
     * The minimum amount of items to have below your current scroll position before loading more.
     */
    protected int mVisibleThreshold = 10;

    /**
     * Grid layout manager
     */
    protected GridLayoutManager mLayoutManager;

    public EndlessRecyclerOnScrollListener(GridLayoutManager gridLayoutManager) {
        mLayoutManager = gridLayoutManager;
    }

    /**
     * {@inheritDoc}
     *
     * Invokes {@link #onLoadMore()} if user is almost at the end of the list
     */
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = mLayoutManager.getItemCount();
        int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

        if (mLoading) {
            if (totalItemCount > mPreviousTotal) {
                mLoading = false;
                mPreviousTotal = totalItemCount;
            }
        }

        if (!mLoading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + mVisibleThreshold)) {
            onLoadMore();
            mLoading = true;
        }
    }

    /**
     * Invoked when loading more items is required
     */
    public abstract void onLoadMore();

    /**
     * Resets state of listener
     */
    public void reset() {
        mPreviousTotal = 0;
        mLoading = true;
    }
}