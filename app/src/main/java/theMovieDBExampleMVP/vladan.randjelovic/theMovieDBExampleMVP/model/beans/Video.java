/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.model.beans;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import popmovies.udacity.theMovieDBExampleMVP.model.beans.mappers.VideoMapper;

/**
 * Abstraction of a single video object for a movie.
 */
public class Video implements Parcelable {

    private static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    /**
     * ID
     */
    @SerializedName("id")
    protected String mId;

    /**
     * Name of video
     */
    @SerializedName("name")
    protected String mName;

    /**
     * Youtube key
     */
    @SerializedName("key")
    protected String mYoutubeKey;

    /**
     * Local database movie ID
     */
    protected String mMovieId;

    public Video() {
    }

    /**
     * Creates new instance of object from {@link Cursor}
     * @param cursor {@link Cursor}
     */
    public Video(Cursor cursor) {
        VideoMapper.constructFromCursor(cursor, this);
    }

    public void setId(String id) {
        mId = id;
    }

    public String getId() {
        return mId;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setYoutubeKey(String youtubeKey) {
        mYoutubeKey = youtubeKey;
    }

    public String getYoutubeKey() {
        return mYoutubeKey;
    }

    public void setMovieId(String movieId) {
        mMovieId = movieId;
    }

    public String getMovieId() {
        return mMovieId;
    }

    /**
     * Creates and returns formatted Youtube URL string
     * @return Youtube URL string of a video
     */
    public String getYoutubeUrl() {
        return String.format("%s%s", YOUTUBE_URL, mYoutubeKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        VideoMapper.writeToParcel(dest, this);
    }

    /**
     * Creator that generates instances of class from a Parcel
     */
    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return VideoMapper.constructFromParcel(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
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
     * Creates {@link ContentValues} for a given video
     * @return {@link ContentValues} for video
     */
    public ContentValues getContentValues() {
        return VideoMapper.constructContentValues(this);
    }
}
