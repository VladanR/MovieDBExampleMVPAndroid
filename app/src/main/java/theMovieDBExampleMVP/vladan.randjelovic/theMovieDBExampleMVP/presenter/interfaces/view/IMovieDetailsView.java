/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.presenter.interfaces.view;

import android.graphics.Bitmap;

import popmovies.udacity.theMovieDBExampleMVP.model.beans.Movie;

/**
 * Interface defining available actions on movies details view
 */
public interface IMovieDetailsView extends IView {

    /**
     * Renders a movie on the view
     * @param movie Movie to render
     */
    void renderMovie(Movie movie);

    /**
     * Returns rendered bitmap of a movie
     * @return {@link Bitmap}
     */
    Bitmap getMovieBitmap();
}
