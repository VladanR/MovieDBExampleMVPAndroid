/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.model.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Abstraction of an TheMovieDb API response when querying API for generic object.
 */
public abstract class BaseResponse<T> {

    /**
     * List of results
     */
    @SerializedName("results")
    List<T> mResultList;

    public BaseResponse() {
    }

    public List<T> getResults() {
        return mResultList;
    }
}
