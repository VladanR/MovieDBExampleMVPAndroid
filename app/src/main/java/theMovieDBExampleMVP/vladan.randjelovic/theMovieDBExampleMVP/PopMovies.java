/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP;

import android.app.Application;

/**
 * Application class, defining top most common logic
 */
public class PopMovies extends Application {

    /**
     * Dependency injection graph
     */
    private Graph mGraph;

    /**
     * Self static instance
     */
    private static PopMovies sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mGraph = Graph.Initializer.init(this);
        sInstance = this;
    }

    /**
     * Returns instance of {@link PopMovies} application object
     * @return {@link PopMovies} application object
     */
    public static PopMovies getInstance() {
        return sInstance;
    }

    /**
     * Returns dependency injection graph
     * @return {@link Graph} object
     */
    public Graph graph() {
        return mGraph;
    }
}
