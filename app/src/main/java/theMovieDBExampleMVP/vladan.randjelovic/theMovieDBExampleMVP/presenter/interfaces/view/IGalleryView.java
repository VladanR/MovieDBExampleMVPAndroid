/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.presenter.interfaces.view;

import popmovies.udacity.theMovieDBExampleMVP.model.beans.Gallery;

/**
 * Interface defining available actions on movies gallery view
 */
public interface IGalleryView extends IView {

    /**
     * Invoked when gallery should be rendered
     * @param gallery Gallery to render
     */
    void renderGallery(Gallery gallery);

    /**
     * Notifies screen that data will refresh and scroll has to be reset
     */
    void resetScroll();
}
