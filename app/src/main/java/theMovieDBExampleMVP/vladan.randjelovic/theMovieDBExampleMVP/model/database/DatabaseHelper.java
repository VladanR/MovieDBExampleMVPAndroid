/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import popmovies.udacity.theMovieDBExampleMVP.model.database.MovieContract.ReviewEntry;
import popmovies.udacity.theMovieDBExampleMVP.model.database.MovieContract.VideoEntry;
import popmovies.udacity.theMovieDBExampleMVP.model.database.MovieContract.MovieEntry;

/**
 * Manages a local database for movie data.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * Database version
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Database name
     */
    private static final String DATABASE_NAME = "popmovies.db";

    /**
     * Create movie table query
     */
    private final static String CREATE_FAVORITE_MOVIES_TABLE_QUERY =
            "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                    MovieEntry._ID + " TEXT PRIMARY KEY," +
                    MovieEntry.COLUMN_TITLE + " TEXT, " +
                    MovieEntry.COLUMN_POSTER_PATH + " TEXT, " +
                    MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                    MovieEntry.COLUMN_USER_RATING + " REAL, " +
                    MovieEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                    " UNIQUE (" + MovieEntry._ID + ") ON CONFLICT REPLACE);";

    /**
     * Create video table query
     */
    private final static String CREATE_VIDEO_TABLE_QUERY =
            "CREATE TABLE " + VideoEntry.TABLE_NAME + " (" +
                    VideoEntry._ID + " TEXT PRIMARY KEY," +
                    VideoEntry.COLUMN_NAME + " TEXT, " +
                    VideoEntry.COLUMN_YOUTUBE_KEY + " TEXT, " +
                    VideoEntry.COLUMN_MOVIE_ID + " TEXT, " +
                    " FOREIGN KEY (" + VideoEntry.COLUMN_MOVIE_ID+ ") REFERENCES " +
                    MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + "), " +
                    " UNIQUE (" + VideoEntry._ID + ", " +
                    VideoEntry.COLUMN_YOUTUBE_KEY + ") ON CONFLICT REPLACE);";

    /**
     * Create review table query
     */
    private final static String CREATE_REVIEW_TABLE_QUERY =
            "CREATE TABLE " + ReviewEntry.TABLE_NAME + " (" +
                    ReviewEntry._ID + " TEXT PRIMARY KEY," +
                    ReviewEntry.COLUMN_AUTHOR + " TEXT, " +
                    ReviewEntry.COLUMN_CONTENT + " TEXT, " +
                    ReviewEntry.COLUMN_MOVIE_ID + " TEXT, " +
                    " FOREIGN KEY (" + ReviewEntry.COLUMN_MOVIE_ID+ ") REFERENCES " +
                    MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + "), " +
                    " UNIQUE (" + ReviewEntry._ID + ") ON CONFLICT REPLACE);";

    /**
     * {@inheritDoc}
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_FAVORITE_MOVIES_TABLE_QUERY);
        sqLiteDatabase.execSQL(CREATE_REVIEW_TABLE_QUERY);
        sqLiteDatabase.execSQL(CREATE_VIDEO_TABLE_QUERY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + VideoEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
