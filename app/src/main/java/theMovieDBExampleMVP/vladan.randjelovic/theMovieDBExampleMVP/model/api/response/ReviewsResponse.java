/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.model.api.response;

import popmovies.udacity.theMovieDBExampleMVP.model.beans.Review;

/**
 * Abstraction of an TheMovieDb API response when querying for list of reviews for a movie.
 */
public class ReviewsResponse extends BasePaginatedResponse<Review> {

    public ReviewsResponse() {
        super();
    }
}
