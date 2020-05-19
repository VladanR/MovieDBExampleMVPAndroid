/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.presenter.interfaces.presenter;

import android.os.Bundle;

import popmovies.udacity.theMovieDBExampleMVP.presenter.interfaces.view.IView;

/**
 * Interface defining available actions on every presenter
 */
public interface IPresenter {

    /**
     * Method that notifies the presenter that view has been rendered on screen
     */
    void onScreenCreated();

    /**
     * Invoked when instance state of a view gets restored
     * @param savedInstanceState Bundle data used to restore instance state
     */
    void onRestoreInstanceState(Bundle savedInstanceState);

    /**
     * Invoked when instance state should be saved
     * @param outState Bundle in which to place data
     */
    void onSaveInstanceState(Bundle outState);

    /**
     * Invoked when user leaves the view and data should be cleared
     */
    void onScreenDestroy();

    /**
     * Invoked when screen is resumed
     */
    void onScreenResumed();

    /**
     * Sets view for presenter
     */
    <T extends IView> void setView(T view);

    /**
     * Invoked when activity is created.
     */
    void onActivityCreated();
}
