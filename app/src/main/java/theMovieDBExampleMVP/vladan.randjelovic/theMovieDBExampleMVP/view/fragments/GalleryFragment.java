/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;

import butterknife.BindView;
import popmovies.udacity.theMovieDBExampleMVP.PopMovies;
import popmovies.udacity.theMovieDBExampleMVP.R;
import popmovies.udacity.theMovieDBExampleMVP.model.beans.Gallery;
import popmovies.udacity.theMovieDBExampleMVP.presenter.interfaces.presenter.IGalleryPresenter;
import popmovies.udacity.theMovieDBExampleMVP.presenter.interfaces.view.IGalleryView;
import popmovies.udacity.theMovieDBExampleMVP.view.adapter.GalleryAdapter;
import popmovies.udacity.theMovieDBExampleMVP.view.controls.AutofitRecyclerView;
import popmovies.udacity.theMovieDBExampleMVP.view.controls.EndlessRecyclerOnScrollListener;

/**
 * A fragment containing gallery of movies downloaded from API
 */
public class GalleryFragment extends BaseFragment<IGalleryPresenter> implements IGalleryView {

    /**
     * RecyclerView that is used to render movies
     */
    @BindView(R.id.gallery_recycler_view) protected AutofitRecyclerView mGallery;

    /**
     * Scroll listener for lazy loading
     */
    protected EndlessRecyclerOnScrollListener mScrollListener;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void inject() {
        PopMovies.getInstance().graph().inject(this);
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    protected Integer getLayout() {
        return R.layout.fragment_gallery;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPrepareLayoutFinished() {
        initRecyclerView();
    }

    /**
     * Initializes {@link GridLayoutManager} and {@link EndlessRecyclerOnScrollListener}
     * for the {@link #mGallery}
     */
    protected void initRecyclerView() {
        GridLayoutManager manager = (GridLayoutManager) mGallery.getLayoutManager();
        mScrollListener = new EndlessRecyclerOnScrollListener(manager) {
            @Override
            public void onLoadMore() {
                if (mPresenter == null) return;
                mPresenter.loadMoreMovies();
            }
        };

        mGallery.addOnScrollListener(mScrollListener);
        mGallery.getRecycledViewPool().setMaxRecycledViews(0, 1000);
    }

    /**
     * {@inheritDoc}
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.onActivityCreated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetScroll() {
        if (mScrollListener != null) {
            mScrollListener.reset();
        }

        mGallery.scrollToPosition(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderGallery(Gallery gallery) {
        GalleryAdapter adapter = (GalleryAdapter) mGallery.getAdapter();
        if (adapter == null) {
            adapter = new GalleryAdapter(gallery);
            mGallery.setAdapter(adapter);
            return;
        }

        adapter.replaceItems(gallery);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setContentVisibility(int visibility) {
        mGallery.setVisibility(visibility);
    }
}