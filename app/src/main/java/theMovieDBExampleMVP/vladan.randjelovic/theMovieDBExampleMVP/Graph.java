/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import popmovies.udacity.theMovieDBExampleMVP.model.DataModule;
import popmovies.udacity.theMovieDBExampleMVP.model.api.ApiModule;
import popmovies.udacity.theMovieDBExampleMVP.model.api.MoviesApi;
import popmovies.udacity.theMovieDBExampleMVP.model.beans.Gallery;
import popmovies.udacity.theMovieDBExampleMVP.presenter.GalleryPresenter;
import popmovies.udacity.theMovieDBExampleMVP.presenter.MovieDetailsPresenter;
import popmovies.udacity.theMovieDBExampleMVP.presenter.PresenterModule;
import popmovies.udacity.theMovieDBExampleMVP.presenter.interfaces.presenter.IGalleryPresenter;
import popmovies.udacity.theMovieDBExampleMVP.presenter.interfaces.presenter.IMovieDetailsPresenter;
import popmovies.udacity.theMovieDBExampleMVP.view.fragments.GalleryFragment;
import popmovies.udacity.theMovieDBExampleMVP.view.fragments.MovieDetailFragment;

/**
 * Graph component that is used to provide injectable members
 */
@Singleton
@Component(
        modules = {AppModule.class, ApiModule.class,
                DataModule.class, PresenterModule.class }
)
public interface Graph {
    void inject(GalleryPresenter presenter);
    void inject(MovieDetailsPresenter presenter);
    void inject(GalleryFragment fragment);
    void inject(MovieDetailFragment fragment);

    MoviesApi getMoviesApiClient();
    Gallery getGallery();
    Gallery.GalleryType getSavedGalleryType();
    IGalleryPresenter getGalleryPresenter();
    IMovieDetailsPresenter getMovieDetailsPresenter();

    final class Initializer {
        public static Graph init(Application application) {
            return DaggerGraph.builder()
                    .appModule(new AppModule(application))
                    .dataModule(new DataModule())
                    .apiModule(new ApiModule())
                    .presenterModule(new PresenterModule())
                    .build();
        }
    }
}
