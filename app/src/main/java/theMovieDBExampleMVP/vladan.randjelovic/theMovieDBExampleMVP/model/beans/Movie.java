/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.model.beans;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import popmovies.udacity.theMovieDBExampleMVP.model.beans.mappers.MovieMapper;

/**
 * Abstraction of a movie object.
 *
 * Contains details of single movie.
 */
public class Movie implements Parcelable {

    private static final String MOVIE_POSTER_ENDPOINT = "https://image.tmdb.org/t/p/w185";

    /**
     * Id
     */
    @SerializedName("id")
    protected String mId;

    /**
     * Title
     */
    @SerializedName("original_title")
    protected String mTitle;

    /**
     * Poster path
     */
    @SerializedName("poster_path")
    protected String mPosterPath;

    /**
     * Plot overview
     */
    @SerializedName("overview")
    protected String mPlotOverview;

    /**
     * User's rating
     */
    @SerializedName("vote_average")
    protected double mUserRating;

    /**
     * Release date
     */
    @SerializedName("release_date")
    protected String mReleaseDate;

    /**
     * Reviews of a movie
     */
    protected List<Review> mReviews;

    /**
     * Videos of a movie
     */
    protected List<Video> mVideos;

    /**
     * Defines if movie is favorite
     */
    protected boolean mFavorite;

    public Movie() {
    }

    /**
     * Creates new instance of object from {@link Cursor}
     * @param cursor {@link Cursor}
     */
    public Movie(Cursor cursor) {
        MovieMapper.constructFromCursor(cursor, this);
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    public String getPlotOverview() {
        return mPlotOverview;
    }

    public void setPlotOverview(String plotOverview) {
        mPlotOverview = plotOverview;
    }

    public double getUserRating() {
        return mUserRating;
    }

    public void setUserRating(double userRating) {
        mUserRating = userRating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    /**
     * Returns full formatted URL string for movie poster
     * @return Full URL for a movie poster
     */
    public String getMoviePosterFullUrl() {
        return String.format("%s%s", MOVIE_POSTER_ENDPOINT, mPosterPath);
    }

    /**
     * Sets reviews and adds movie ID to each review
     * @param reviews List of reviews to set for the movie
     */
    public void setReviews(List<Review> reviews) {
        mReviews = reviews;
        if (mReviews == null) return;

        for (Review review : mReviews) {
            review.setMovieId(mId);
        }
    }

    public List<Review> getReviews() {
        return mReviews;
    }

    /**
     * Sets the videos and adds movie ID for each movie
     * @param videos List of videos to set for the movie
     */
    public void setVideos(List<Video> videos) {
        mVideos = videos;
        if (mVideos == null) return;

        for (Video video : mVideos) {
            video.setMovieId(mId);
        }
    }

    public List<Video> getVideos() {
        return mVideos;
    }

    public void setFavorite(boolean favorite) {
        mFavorite = favorite;
    }

    public boolean isFavorite() {
        return mFavorite;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        MovieMapper.writeToParcel(dest, this);
    }

    /**
     * Creator that generates instancess of class from a Parcel
     */
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return MovieMapper.constructFromParcel(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Returns content values for a given movie
     * @return {@link ContentValues} for a movie
     */
    public ContentValues getContentValues() {
        return MovieMapper.constructContentValues(this);
    }

    /**
     * Returns content values array for trailers
     * @return Array of {@link ContentValues} for trailers, or <code>null</code> if empty
     */
    @Nullable
    public ContentValues[] getVideosContentValues() {
        if (mVideos == null || mVideos.size() == 0) return null;

        ContentValues[] contentValues = new ContentValues[mVideos.size()];
        for (int i = 0; i < mVideos.size(); i++) {
            contentValues[i] = mVideos.get(i).getContentValues();
        }

        return contentValues;
    }

    /**
     * Returns content values array for reviews
     * @return Array of {@link ContentValues} for reviews, or <code>null</code> if empty
     */
    @Nullable
    public ContentValues[] getReviewsContentValues() {
        if (mReviews == null || mReviews.size() == 0) return null;

        ContentValues[] contentValues = new ContentValues[mReviews.size()];
        for (int i = 0; i < mReviews.size(); i++) {
            contentValues[i] = mReviews.get(i).getContentValues();
        }

        return contentValues;
    }
}
