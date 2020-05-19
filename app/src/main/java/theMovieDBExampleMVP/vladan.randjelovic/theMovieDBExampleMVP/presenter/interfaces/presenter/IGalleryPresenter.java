/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.presenter.interfaces.presenter;

/**
 * Interface defining available actions on movies gallery presenter
 */
public interface IGalleryPresenter extends IPresenter {

    /**
     * Invoked when more movies should be
     */
    void loadMoreMovies();
}
