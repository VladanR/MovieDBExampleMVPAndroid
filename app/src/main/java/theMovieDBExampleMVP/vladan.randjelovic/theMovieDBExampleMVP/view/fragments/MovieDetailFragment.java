/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.view.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import popmovies.udacity.theMovieDBExampleMVP.PopMovies;
import popmovies.udacity.theMovieDBExampleMVP.R;
import popmovies.udacity.theMovieDBExampleMVP.model.beans.Movie;
import popmovies.udacity.theMovieDBExampleMVP.presenter.interfaces.presenter.IMovieDetailsPresenter;
import popmovies.udacity.theMovieDBExampleMVP.presenter.interfaces.view.IMovieDetailsView;
import popmovies.udacity.theMovieDBExampleMVP.view.activities.SettingsActivity;
import popmovies.udacity.theMovieDBExampleMVP.view.adapter.MovieDetailsAdapter;
import popmovies.udacity.theMovieDBExampleMVP.view.controls.EndlessRecyclerOnScrollListener;

/**
 * Shows details of a movie
 */
public class MovieDetailFragment extends BaseFragment<IMovieDetailsPresenter>
        implements IMovieDetailsView, MovieDetailsAdapter.OnFavoriteButtonClick {

    /**
     * Bundle key for the movie
     */
    public static final String EXTRA_MOVIE_KEY = "EXTRA_MOVIE_KEY";

    /**
     * Tag of fragment
     */
    public static final String TAG = MovieDetailFragment.class.getSimpleName();

    /**
     * Share action provider
     */
    private ShareActionProvider mShareActionProvider;

    /**
     * Recycler view that renders details of a movie
     */
    @BindView(R.id.movie_details_list) protected RecyclerView mMovieDetailsList;

    /**
     * Creates new instance of a fragment
     * @param movie Movie that should be loaded
     * @return Movie Detail fragment instance
     */
    public static MovieDetailFragment newInstance(Movie movie) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(EXTRA_MOVIE_KEY, movie);
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

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
        return R.layout.fragment_movies_detail;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPresenter.loadData(getArguments());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPrepareLayoutFinished() {
        initRecyclerView();
    }

    /**
     * Initializes {@link LinearLayoutManager} and {@link EndlessRecyclerOnScrollListener}
     * for the {@link #mMovieDetailsList}
     */
    protected void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mMovieDetailsList.setLayoutManager(manager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderMovie(Movie movie) {
        getActivity().invalidateOptionsMenu();
        MovieDetailsAdapter adapter = (MovieDetailsAdapter) mMovieDetailsList.getAdapter();
        if (adapter == null) {
            adapter = new MovieDetailsAdapter(movie, this);
            mMovieDetailsList.setAdapter(adapter);
            if (mShareActionProvider != null) {
                setupShare();
            }

            return;
        }

        adapter.replaceMovie(movie);
    }

    /**
     * Setups share action provider if trailer URL is available
     */
    protected void setupShare() {
        String trailerUrl = mPresenter.getTrailerUrl();
        if (trailerUrl != null) {
            mShareActionProvider.setShareIntent(createShareTrailerIntent());
        }
    }

    /**
     * Creates intent for share action
     * @return {@link Intent}
     */
    protected Intent createShareTrailerIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mPresenter.getTrailerUrl());
        return shareIntent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setContentVisibility(int visibility) {
        mMovieDetailsList.setVisibility(visibility);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!mPresenter.doesMovieHasTrailers()) {
            inflater.inflate(R.menu.activity_popmovies, menu);
            return;
        }

        inflater.inflate(R.menu.activity_movie_details, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        setupShare();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        } else if (id == R.id.action_refresh) {
            refresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFavoriteButtonClick() {
        mPresenter.onFavoritesClicked();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bitmap getMovieBitmap() {
        return ((MovieDetailsAdapter) mMovieDetailsList.getAdapter())
                .getMovieBitmap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.onActivityCreated();
    }
}
