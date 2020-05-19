/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.model.api;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Module that provides API clients to the app
 */
@Module
public class ApiModule {

    private static final String ENDPOINT = "http://api.themoviedb.org/3/";

    /**
     * Provides API Retrofit client for communicating with API
     * @return MoviesAPI client
     */
    @Singleton
    @Provides
    public MoviesApi provideMoviesApiClient() {
        return new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MoviesApi.class);
    }
}
