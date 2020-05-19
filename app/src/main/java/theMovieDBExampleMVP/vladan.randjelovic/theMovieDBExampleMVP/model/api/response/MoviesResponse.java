/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.model.api.response;

import popmovies.udacity.theMovieDBExampleMVP.model.beans.Movie;

/**
 * Abstraction of an TheMovieDb API response when querying for list of movies.
 */
public class MoviesResponse extends BasePaginatedResponse<Movie> {

    public MoviesResponse() {
        super();
    }
}
