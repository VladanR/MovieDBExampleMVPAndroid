/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.model.beans;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import popmovies.udacity.theMovieDBExampleMVP.model.beans.mappers.ReviewMapper;

/**
 * Abstraction of a single review object for a movie.
 */
public class Review implements Parcelable {

    /**
     * ID
     */
    @SerializedName("id")
    protected String mId;

    /**
     * Author of review
     */
    @SerializedName("author")
    protected String mAuthor;

    /**
     * Review text
     */
    @SerializedName("content")
    protected String mContent;

    /**
     * Local database movie ID
     */
    protected String mMovieId;

    public Review() {
    }

    /**
     * Creates new instance of object from {@link Cursor}
     * @param cursor {@link Cursor}
     */
    public Review(Cursor cursor) {
        ReviewMapper.constructFromCursor(cursor, this);
    }

    public void setId(String id) {
        mId = id;
    }

    public String getId() {
        return mId;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getContent() {
        return mContent;
    }

    public void setMovieId(String movieId) {
        mMovieId = movieId;
    }

    public String getMovieId() {
        return mMovieId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ReviewMapper.writeToParcel(dest, this);
    }

    /**
     * Creator that generates instances of class from a Parcel
     */
    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return ReviewMapper.constructFromParcel(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
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
     * Creates {@link ContentValues} for a given review
     * @return {@link ContentValues} for review
     */
    public ContentValues getContentValues() {
        return ReviewMapper.constructContentValues(this);
    }
}
