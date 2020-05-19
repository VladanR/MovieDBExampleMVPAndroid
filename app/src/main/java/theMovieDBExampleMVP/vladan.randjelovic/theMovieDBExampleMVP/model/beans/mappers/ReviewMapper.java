/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.model.beans.mappers;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.support.annotation.NonNull;

import popmovies.udacity.theMovieDBExampleMVP.model.beans.Review;
import popmovies.udacity.theMovieDBExampleMVP.model.database.MovieContract;

/**
 * Defines set of static methods used to create and map {@link Review}
 */
public class ReviewMapper {

    /**
     * ID column index
     */
    private static final int COLUMN_ID = 0;

    /**
     * Author column index
     */
    private static final int COLUMN_AUTHOR = 1;

    /**
     * Content column index
     */
    private static final int COLUMN_CONTENT = 2;

    /**
     * Movie ID column index
     */
    private static final int COLUMN_MOVIE_ID = 3;

    /**
     * Columns for query
     */
    public static final String[] REVIEW_COLUMNS = {
            MovieContract.ReviewEntry.TABLE_NAME + "." + MovieContract.ReviewEntry._ID,
            MovieContract.ReviewEntry.COLUMN_AUTHOR,
            MovieContract.ReviewEntry.COLUMN_CONTENT,
            MovieContract.ReviewEntry.COLUMN_MOVIE_ID
    };

    /**
     * Writes {@link Review} object to {@link Parcel}
     * @param parcel Parcel object
     * @param review Review object
     */
    public static void writeToParcel(@NonNull Parcel parcel, @NonNull Review review) {
        parcel.writeString(review.getId());
        parcel.writeString(review.getAuthor());
        parcel.writeString(review.getContent());
        parcel.writeString(review.getMovieId());
    }

    /**
     * Constructs {@link Review} from {@link Parcel}
     * @param parcel Parcel object
     * @return Review object constructed from {@link Parcel}
     */
    public static Review constructFromParcel(@NonNull Parcel parcel) {
        Review review = new Review();
        review.setId(parcel.readString());
        review.setAuthor(parcel.readString());
        review.setContent(parcel.readString());
        review.setMovieId(parcel.readString());
        return review;
    }

    /**
     * Construct {@link ContentValues} from a given review
     * @param review Review
     * @return {@link ContentValues}
     */
    public static ContentValues constructContentValues(@NonNull Review review) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.ReviewEntry._ID, review.getId());
        contentValues.put(MovieContract.ReviewEntry.COLUMN_AUTHOR, review.getAuthor());
        contentValues.put(MovieContract.ReviewEntry.COLUMN_CONTENT, review.getContent());
        contentValues.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, review.getMovieId());
        return contentValues;
    }

    /**
     * Constructs a review from a cursor
     * @param cursor {@link Cursor}
     * @param review {@link Review} object to save data to
     * @return Review with data from cursor
     */
    public static Review constructFromCursor(@NonNull Cursor cursor, @NonNull Review review) {
        review.setId(cursor.getString(COLUMN_ID));
        review.setAuthor(cursor.getString(COLUMN_AUTHOR));
        review.setContent(cursor.getString(COLUMN_CONTENT));
        review.setMovieId(cursor.getString(COLUMN_MOVIE_ID));
        return review;
    }
}
