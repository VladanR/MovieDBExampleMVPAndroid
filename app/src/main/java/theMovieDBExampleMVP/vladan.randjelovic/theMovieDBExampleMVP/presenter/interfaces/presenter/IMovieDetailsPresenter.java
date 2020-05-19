/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.presenter.interfaces.presenter;

import android.os.Bundle;

/**
 * Interface defining available actions on movies detail presenter
 */
public interface IMovieDetailsPresenter extends IPresenter {

    /**
     * Loads data from bundle which was sent
     * @param bundle Bundled data
     */
    void loadData(Bundle bundle);

    /**
     * Invoked when user pressed favorites button. It will
     * either add or remove movie from favorites list, depending on previous
     * state.
     */
    void onFavoritesClicked();

    /**
     * Returns trailer URL
     * @return Trailer URL as string
     */
    String getTrailerUrl();

    /**
     * Checks if movie has trailers
     * @return <code>true</code> if movie has trailers, <code>false</code> otherwise
     */
    boolean doesMovieHasTrailers();
}