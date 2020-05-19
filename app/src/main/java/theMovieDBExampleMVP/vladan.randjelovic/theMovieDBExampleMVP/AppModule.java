/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Application module
 */
@Module
public class AppModule {

    /**
     * Application
     */
    private final Application mApp;

    public AppModule(Application app) {
        mApp = app;
    }

    /**
     * Provides {@link Application} object
     * @return {@link Application}
     */
    @Provides
    @Singleton
    public Application provideApplication() {
        return mApp;
    }

    /**
     * Provides {@link Context}
     * @return {@link Context}
     */
    @Provides
    public Context provideContext() {
        return mApp.getApplicationContext();
    }
}
