/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.model.beans.mappers;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.support.annotation.NonNull;

import java.util.List;

import popmovies.udacity.theMovieDBExampleMVP.model.beans.Movie;
import popmovies.udacity.theMovieDBExampleMVP.model.beans.Review;
import popmovies.udacity.theMovieDBExampleMVP.model.beans.Video;
import popmovies.udacity.theMovieDBExampleMVP.model.database.MovieContract;
import popmovies.udacity.theMovieDBExampleMVP.presenter.Constants;

/**
 * Defines set of static methods used to create and map {@link Movie}
 */
public class MovieMapper {

    /**
     * Movie ID column index
     */
    private static final int COLUMN_MOVIE_ID = 0;

    /**
     * Movie title column index
     */
    private static final int COLUMN_TITLE = 1;

    /**
     * Movie poster column index
     */
    private static final int COLUMN_POSTER_PATH = 2;

    /**
     * Movie overview column index
     */
    private static final int COLUMN_OVERVIEW = 3;

    /**
     * Movie user rating column index
     */
    private static final int COLUMN_USER_RATING = 4;

    /**
     * Movie release date column index
     */
    private static final int COLUMN_RELEASE_DATE = 5;

    /**
     * Columns for a database query
     */
    public static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_USER_RATING,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE
    };

    /**
     * Writes {@link Movie} object to {@link Parcel}
     * @param parcel Parcel object
     * @param movie Movie object
     */
    public static void writeToParcel(@NonNull Parcel parcel, @NonNull Movie movie) {
        parcel.writeString(movie.getId());
        parcel.writeString(movie.getTitle());
        parcel.writeString(movie.getPosterPath());
        parcel.writeString(movie.getPlotOverview());
        parcel.writeDouble(movie.getUserRating());
        parcel.writeString(movie.getReleaseDate());
        parcel.writeByte(movie.isFavorite() ? Constants.BOOLEAN_TRUE : Constants.BOOLEAN_FALSE);
        parcel.writeTypedList(movie.getReviews());
        parcel.writeTypedList(movie.getVideos());
    }

    /**
     * Constructs {@link Movie} from {@link Parcel}
     * @param parcel Parcel object
     * @return Movie object constructed from {@link Parcel}
     */
    public static Movie constructFromParcel(@NonNull Parcel parcel) {
        Movie movie = new Movie();
        movie.setId(parcel.readString());
        movie.setTitle(parcel.readString());
        movie.setPosterPath(parcel.readString());
        movie.setPlotOverview(parcel.readString());
        movie.setUserRating(parcel.readDouble());
        movie.setReleaseDate(parcel.readString());
        movie.setFavorite(parcel.readByte() == Constants.BOOLEAN_TRUE);
        List<Review> reviews = parcel.createTypedArrayList(Review.CREATOR);
        movie.setReviews(reviews);

        List<Video> videos = parcel.createTypedArrayList(Video.CREATOR);
        movie.setVideos(videos);
        return movie;
    }

    /**
     * Construct {@link ContentValues} from a given movie
     * @param movie Movie
     * @return {@link ContentValues}
     */
    public static ContentValues constructContentValues(@NonNull Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry._ID, movie.getId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getPlotOverview());
        contentValues.put(MovieContract.MovieEntry.COLUMN_USER_RATING, movie.getUserRating());
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        return contentValues;
    }

    /**
     * Constructs a movie from a cursor
     * @param cursor {@link Cursor}
     * @param movie {@link Movie} object to save data to
     * @return Movie with data from cursor
     */
    public static Movie constructFromCursor(@NonNull Cursor cursor, @NonNull Movie movie) {
        movie.setId(cursor.getString(COLUMN_MOVIE_ID));
        movie.setTitle(cursor.getString(COLUMN_TITLE));
        movie.setPlotOverview(cursor.getString(COLUMN_OVERVIEW));
        movie.setReleaseDate(cursor.getString(COLUMN_RELEASE_DATE));
        movie.setUserRating(cursor.getDouble(COLUMN_USER_RATING));
        movie.setPosterPath(cursor.getString(COLUMN_POSTER_PATH));
        movie.setFavorite(true);
        return movie;
    }
}
