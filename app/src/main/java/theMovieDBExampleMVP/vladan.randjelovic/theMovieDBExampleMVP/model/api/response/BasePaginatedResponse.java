/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.model.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * Abstraction of a paginated TheMovieDb API response when querying API for generic object.
 */
public abstract class BasePaginatedResponse<T> extends BaseResponse<T> {

    /**
     * Total number of pages
     */
    @SerializedName("total_pages")
    int mTotalNumberOfPages;

    /**
     * Current page
     */
    @SerializedName("page")
    int mCurrentPage;

    public BasePaginatedResponse() {
        super();
    }

    public int getLastPage() {
        return mTotalNumberOfPages;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }
}
