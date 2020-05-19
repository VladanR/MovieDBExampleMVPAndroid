/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.presenter.interfaces.view;

import android.content.Context;

/**
 * Interface defining available actions on any view
 */
public interface IView {

    /**
     * Invoked when progress bar should be hidden and content shown
     */
    void hideProgressBar();

    /**
     * Invoked when progress bar should be shown and content hidden
     */
    void showProgressBar();

    /**
     * Invoked when API is not responding and message
     * should be shown
     */
    void showServerErrorMessage();

    /**
     * Invoked when there is no internet connection
     * and user should be notified
     */
    void showNoInternetConnection();

    /**
     * Returns context of a view
     * @return Context
     */
    Context getContext();
}
