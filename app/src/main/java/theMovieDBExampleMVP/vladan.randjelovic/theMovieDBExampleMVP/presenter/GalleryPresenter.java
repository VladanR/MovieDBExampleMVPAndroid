/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.presenter;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import popmovies.udacity.theMovieDBExampleMVP.PopMovies;
import popmovies.udacity.theMovieDBExampleMVP.model.api.response.BaseResponse;
import popmovies.udacity.theMovieDBExampleMVP.model.api.response.MoviesResponse;
import popmovies.udacity.theMovieDBExampleMVP.model.beans.Gallery;
import popmovies.udacity.theMovieDBExampleMVP.model.beans.Movie;
import popmovies.udacity.theMovieDBExampleMVP.model.beans.mappers.MovieMapper;
import popmovies.udacity.theMovieDBExampleMVP.model.database.MovieContract;
import popmovies.udacity.theMovieDBExampleMVP.presenter.interfaces.presenter.IGalleryPresenter;
import popmovies.udacity.theMovieDBExampleMVP.presenter.interfaces.view.IGalleryView;
import retrofit2.Response;
import rx.Observable;

/**
 * Presenter that controls flow of gallery screen
 */
public class GalleryPresenter extends BasePresenter<IGalleryView> implements IGalleryPresenter,
        LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Key for gallery bundle values
     */
    private static final String EXTRA_GALLERY = "EXTRA_GALLERY";


    /**
     * Defines how to sort movies on screen
     */
    @Inject protected Gallery.GalleryType mSortBy;

    /**
     * Gallery that is currently rendered
     */
    @Inject protected Gallery mGallery;

    /**
     * Movie's loader ID
     */
    private static final int MOVIES_LOADER = 0;

    /**
     * Constructs presenter instance
     */
    public GalleryPresenter() {
        super();
        PopMovies.getInstance().graph().inject(this);
    }

    /**
     * Invokes loader manager for content provider
     */
    @Override
    public void onActivityCreated() {
        LoaderManager manager = ((AppCompatActivity) getView().getContext())
                .getSupportLoaderManager();
        manager.initLoader(MOVIES_LOADER, null, this);
    }

    /**
     * {@inheritDoc}
     *
     * Loads movies if screen is created
     */
    @Override
    protected void onViewCreated() {
        clearGallery();
        loadMovies();
        getView().showProgressBar();
    }

    /**
     * Loads movies if {@link Gallery} is initialized and has more in context of pages
     */
    @Override
    public void loadMoreMovies() {
        if (mGallery != null && mGallery.hasMore()) {
            loadMovies();
        }
    }

    /**
     * Loads additional movies
     */
    protected void loadMovies() {
        switch (mSortBy) {
            case POPULAR:
                loadPopularMovies();
                break;
            case TOP_RATED:
                loadTopRatedMovies();
                break;
            case FAVORITES:
                loadFavoriteMovies();
                break;
        }
    }

    /**
     * Invokes API call for loading popular movies
     */
    protected void loadPopularMovies() {
        Observable<Response<MoviesResponse>> retrofitObservable
                = mApi.getPopularMovies(Constants.API_KEY, mGallery.getNextPageToLoad());
        makeApiCall(retrofitObservable);
    }

    /**
     * Invokes API call for loading top rated movies
     */
    protected void loadTopRatedMovies() {
        Observable<Response<MoviesResponse>> retrofitObservable
                = mApi.getTopRatedMovies(Constants.API_KEY, mGallery.getNextPageToLoad());
        makeApiCall(retrofitObservable);
    }

    /**
     * Initiates call for loading favorite movies from DB
     */
    protected void loadFavoriteMovies() {
        LoaderManager manager = ((AppCompatActivity) getView().getContext())
                .getSupportLoaderManager();
        manager.restartLoader(MOVIES_LOADER, null, this);
    }

    /**
     * When response is obtained from API it renders it on screen
     */
    @Override
    protected <T extends BaseResponse> void onApiResponse(T apiResponse) {
        MoviesResponse moviesResponse = (MoviesResponse) apiResponse;
        updateGalleryWithResponse(moviesResponse);
        renderGallery();
        getView().hideProgressBar();
    }

    /**
     * Updates local gallery object with API data
     * @param response API response for movies
     */
    protected void updateGalleryWithResponse(MoviesResponse response) {
        mGallery.addMovies(response.getResults());
        mGallery.setLastLoadedPage(response.getCurrentPage());
        mGallery.setHasMore(response.getCurrentPage() !=
                response.getLastPage());
    }

    /**
     * Renders gallery to the view
     */
    protected void renderGallery() {
        getView().renderGallery(mGallery);
    }

    /**
     * {@inheritDoc}
     *
     * Renders movies if state is restored
     */
    @Override
    protected void onViewRestored() {
        renderGallery();
        getView().hideProgressBar();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void restoreData(Bundle savedInstanceState) {
        mGallery = savedInstanceState.getParcelable(EXTRA_GALLERY);
    }

    /**
     * Saves state of the gallery screen
     * @param outState Bundle in which to place data
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(EXTRA_GALLERY, mGallery);
    }

    /**
     * When screen is resumed if gallery type is changed data should refresh
     */
    @Override
    public void onScreenResumed() {
        if (!isViewAttached()) return;

        if (isSortByChanged()) {
            clearGallery();
            renderGallery();
            changeSortBy();
            reloadData();
        }
    }

    /**
     * Checks if user changed criteria for showing movies
     * @return <code>true</code> if user changed criteria, <code>false</code> otherwise
     */
    protected boolean isSortByChanged() {
        Gallery.GalleryType savedGalleryType = PopMovies.getInstance()
                .graph()
                .getSavedGalleryType();
        return mSortBy.ordinal() != savedGalleryType.ordinal();
    }

    /**
     * Resets gallery
     */
    protected void clearGallery() {
        mGallery = new Gallery();
    }

    /**
     * Changes current sort criteria value
     */
    protected void changeSortBy() {
        mSortBy = PopMovies.getInstance()
                .graph()
                .getSavedGalleryType();
    }

    /**
     * Reloads data of movies and replaces them with new ones
     */
    protected void reloadData() {
        getView().resetScroll();
        onScreenCreated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = MovieContract.MovieEntry._ID + " ASC";
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;

        return new CursorLoader(getView().getContext(),
                uri,
                MovieMapper.MOVIE_COLUMNS,
                null,
                null,
                sortOrder);
    }

    /**
     * Renders list of favorite movies on screen when loading from DB is finished
     * @param loader Loader
     * @param cursor Cursor
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (getView() == null || mSortBy != Gallery.GalleryType.FAVORITES) return;

        clearGallery();
        if (cursor == null) {
            renderGallery();
            getView().hideProgressBar();
            return;
        }

        List<Movie> movies = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Movie movie = new Movie(cursor);
            movies.add(movie);
            cursor.moveToNext();
        }

        mGallery.addMovies(movies);
        mGallery.setLastLoadedPage(1);
        mGallery.setHasMore(false);
        renderGallery();
        getView().hideProgressBar();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}