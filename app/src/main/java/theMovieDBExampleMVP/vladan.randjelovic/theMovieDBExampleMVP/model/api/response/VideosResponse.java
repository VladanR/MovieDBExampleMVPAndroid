/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.model.api.response;

import popmovies.udacity.theMovieDBExampleMVP.model.beans.Video;

/**
 * Abstraction of an TheMovieDb API response when querying for list of movie's videos.
 */
public class VideosResponse extends BaseResponse<Video> {

    public VideosResponse() {
        super();
    }

}
