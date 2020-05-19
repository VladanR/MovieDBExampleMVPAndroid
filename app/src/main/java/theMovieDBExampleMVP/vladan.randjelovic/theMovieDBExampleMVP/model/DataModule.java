/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import popmovies.udacity.theMovieDBExampleMVP.R;
import popmovies.udacity.theMovieDBExampleMVP.model.annotations.GalleryTypeKey;
import popmovies.udacity.theMovieDBExampleMVP.model.beans.Gallery;

/**
 * Module that provides various data structure objects to the app
 */
@Module
public class DataModule {

    /**
     * Provides singleton shared preferences instance
     * @param context Context
     * @return {@link SharedPreferences}
     */
    @Singleton
    @Provides
    public SharedPreferences provideSharedPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Provides singleton string which is a key in {@link SharedPreferences} for
     * {@link Gallery.GalleryType}
     * @param context Context
     * @return {@link Gallery.GalleryType} shared preferences key
     */
    @Singleton
    @GalleryTypeKey
    @Provides
    public String provideGalleryTypeKey(Context context) {
        return context.getString(R.string.pref_gallery_type_key);
    }

    /**
     * Provides a {@link Gallery.GalleryType} that is saved in local data
     * @param sharedPreferences {@link SharedPreferences}
     * @param key Shared preferences key
     * @return {@link Gallery.GalleryType} that user chose
     */
    @Provides
    public Gallery.GalleryType provideGalleryType(SharedPreferences sharedPreferences,
                                                  @GalleryTypeKey String key) {
        String settingsGalleryType = sharedPreferences.getString(key, null);
        return Gallery.GalleryType.fromString(settingsGalleryType);
    }

    @Provides
    @Singleton
    public Gallery provideGallery() {
        return new Gallery();
    }
}
