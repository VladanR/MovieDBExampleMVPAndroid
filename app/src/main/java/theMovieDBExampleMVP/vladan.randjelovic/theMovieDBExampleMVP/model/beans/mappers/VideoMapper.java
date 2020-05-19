/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.model.beans.mappers;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.support.annotation.NonNull;

import popmovies.udacity.theMovieDBExampleMVP.model.beans.Video;
import popmovies.udacity.theMovieDBExampleMVP.model.database.MovieContract;

/**
 * Defines set of static methods used to create and map {@link Video}
 */
public class VideoMapper {

    /**
     * ID column index
     */
    private static final int COLUMN_ID = 0;

    /**
     * Name column index
     */
    private static final int COLUMN_NAME = 1;

    /**
     * Youtube key column index
     */
    private static final int COLUMN_YOUTUBE_KEY = 2;

    /**
     * Movie ID column index
     */
    private static final int COLUMN_MOVIE_ID = 3;

    /**
     * Columns for query
     */
    public static final String[] VIDEO_COLUMNS = {
            MovieContract.VideoEntry.TABLE_NAME + "." + MovieContract.VideoEntry._ID,
            MovieContract.VideoEntry.COLUMN_NAME,
            MovieContract.VideoEntry.COLUMN_YOUTUBE_KEY,
            MovieContract.VideoEntry.COLUMN_MOVIE_ID
    };

    /**
     * Writes {@link Video} object to {@link Parcel}
     * @param parcel Parcel object
     * @param video Video object
     */
    public static void writeToParcel(@NonNull Parcel parcel, @NonNull Video video) {
        parcel.writeString(video.getId());
        parcel.writeString(video.getName());
        parcel.writeString(video.getYoutubeKey());
        parcel.writeString(video.getMovieId());
    }

    /**
     * Constructs {@link Video} from {@link Parcel}
     * @param parcel Parcel object
     * @return Video object constructed from {@link Parcel}
     */
    public static Video constructFromParcel(@NonNull Parcel parcel) {
        Video video = new Video();
        video.setId(parcel.readString());
        video.setName(parcel.readString());
        video.setYoutubeKey(parcel.readString());
        video.setMovieId(parcel.readString());
        return video;
    }

    /**
     * Construct {@link ContentValues} from a given video
     * @param video Video
     * @return {@link ContentValues}
     */
    public static ContentValues constructContentValues(@NonNull Video video) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.VideoEntry._ID, video.getId());
        contentValues.put(MovieContract.VideoEntry.COLUMN_NAME, video.getName());
        contentValues.put(MovieContract.VideoEntry.COLUMN_YOUTUBE_KEY, video.getYoutubeKey());
        contentValues.put(MovieContract.VideoEntry.COLUMN_MOVIE_ID, video.getMovieId());
        return contentValues;
    }

    /**
     * Constructs a video from a cursor
     * @param cursor {@link Cursor}
     * @param video {@link Video} object to save data to
     * @return Video with data from cursor
     */
    public static Video constructFromCursor(@NonNull Cursor cursor, @NonNull Video video) {
        video.setId(cursor.getString(COLUMN_ID));
        video.setName(cursor.getString(COLUMN_NAME));
        video.setYoutubeKey(cursor.getString(COLUMN_YOUTUBE_KEY));
        video.setMovieId(cursor.getString(COLUMN_MOVIE_ID));
        return video;
    }
}
