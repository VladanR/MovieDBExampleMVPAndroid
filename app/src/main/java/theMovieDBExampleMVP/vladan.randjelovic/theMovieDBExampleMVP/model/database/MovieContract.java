/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.model.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the movie database.
 */
public class MovieContract {

    /**
     * Content authority for movie content provider
     */
    public static final String CONTENT_AUTHORITY = "popmovies.udacity.com";

    /**
     * Base content URI
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Movie content provider subpath
     */
    public static final String PATH_MOVIE = "movie";

    /**
     * Video content provider subpath
     */
    public static final String PATH_VIDEO = "video";

    /**
     * Review content provider subpath
     */
    public static final String PATH_REVIEW = "review";

    /**
     * Defines table contents of the review table
     */
    public static final class ReviewEntry implements BaseColumns {

        /**
         * Content URI
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        /**
         * Table name
         */
        public static final String TABLE_NAME = "review";

        /**
         * Author column
         */
        public static final String COLUMN_AUTHOR = "author";

        /**
         * Content column
         */
        public static final String COLUMN_CONTENT = "content";

        /**
         * Column for foreign movie ID key
         */
        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static Uri buildReviewUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /**
     * Defines table contents of the video table
     */
    public static final class VideoEntry implements BaseColumns {

        /**
         * Content URI
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIDEO).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEO;

        /**
         * Table name
         */
        public static final String TABLE_NAME = "video";

        /**
         * Name column
         */
        public static final String COLUMN_NAME = "name";

        /**
         * Youtube key column
         */
        public static final String COLUMN_YOUTUBE_KEY = "youtube_key";

        /**
         * Column for foreign movie ID key
         */
        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static Uri buildVideoUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /**
     * Defines table contents of the video table
     */
    public static final class MovieEntry implements BaseColumns {

        /**
         * Content URI
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        /**
         * Table name
         */
        public static final String TABLE_NAME = "movie";

        /**
         * Title column
         */
        public static final String COLUMN_TITLE = "title";

        /**
         * Poster path column
         */
        public static final String COLUMN_POSTER_PATH = "poster_path";

        /**
         * Overview column
         */
        public static final String COLUMN_OVERVIEW = "overview";

        /**
         * User rating column
         */
        public static final String COLUMN_USER_RATING = "user_rating";

        /**
         * Release date column
         */
        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
