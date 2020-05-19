/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import popmovies.udacity.theMovieDBExampleMVP.R;
import popmovies.udacity.theMovieDBExampleMVP.model.beans.Movie;
import popmovies.udacity.theMovieDBExampleMVP.view.adapter.GalleryAdapter;
import popmovies.udacity.theMovieDBExampleMVP.view.fragments.GalleryFragment;
import popmovies.udacity.theMovieDBExampleMVP.view.fragments.MovieDetailFragment;

/**
 * Shows the movies in a grid obtained from the API.
 */
public class PopMoviesActivity extends AppCompatActivity
        implements GalleryAdapter.OnMovieClickedListener {

    /**
     * Toolbar of the activity
     */
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    /**
     * Defines if tablet mode is active
     */
    protected boolean mIsTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_movies);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        if (ButterKnife.findById(this, R.id.movie_detail_fragment_container) != null) {
            mIsTablet = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_fragment_container,
                                new MovieDetailFragment(),
                                MovieDetailFragment.TAG)
                        .commit();
            }
        } else {
            mIsTablet = false;
        }
    }

    /**
     * Finds gallery fragment on screen
     * @return Gallery fragment
     */
    protected GalleryFragment getGalleryFragment() {
        return ((GalleryFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_gallery));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mIsTablet) {
            return true;
        }

        getMenuInflater().inflate(R.menu.activity_popmovies, menu);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_refresh) {
            refresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Refreshes the content of fragments
     */
    protected void refresh() {
        GalleryFragment galleryFragment = getGalleryFragment();
        galleryFragment.refresh();

        if (!mIsTablet) return;
        MovieDetailFragment movieDetailFragment = (MovieDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.movie_detail_fragment_container);

        if (movieDetailFragment == null) return;

        movieDetailFragment.refresh();
    }

    /**
     * When movie is clicked it is shown in details fragment
     * @param movie Movie to show
     */
    @Override
    public void onMovieClicked(Movie movie) {
        if (mIsTablet) {
            MovieDetailFragment fragment = MovieDetailFragment.newInstance(movie);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_fragment_container,
                            fragment,
                            MovieDetailFragment.TAG)
                    .commit();
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putParcelable(MovieDetailFragment.EXTRA_MOVIE_KEY, movie);

        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTabletMode() {
        return mIsTablet;
    }
}
