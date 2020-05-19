/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.presenter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import popmovies.udacity.theMovieDBExampleMVP.PopMovies;
import popmovies.udacity.theMovieDBExampleMVP.model.api.response.BaseResponse;
import popmovies.udacity.theMovieDBExampleMVP.model.api.response.ReviewsResponse;
import popmovies.udacity.theMovieDBExampleMVP.model.api.response.VideosResponse;
import popmovies.udacity.theMovieDBExampleMVP.model.beans.Movie;
import popmovies.udacity.theMovieDBExampleMVP.model.beans.Review;
import popmovies.udacity.theMovieDBExampleMVP.model.beans.Video;
import popmovies.udacity.theMovieDBExampleMVP.model.beans.mappers.MovieMapper;
import popmovies.udacity.theMovieDBExampleMVP.model.beans.mappers.ReviewMapper;
import popmovies.udacity.theMovieDBExampleMVP.model.beans.mappers.VideoMapper;
import popmovies.udacity.theMovieDBExampleMVP.model.database.MovieContract;
import popmovies.udacity.theMovieDBExampleMVP.presenter.interfaces.presenter.IMovieDetailsPresenter;
import popmovies.udacity.theMovieDBExampleMVP.presenter.interfaces.view.IMovieDetailsView;
import popmovies.udacity.theMovieDBExampleMVP.view.Utils;
import popmovies.udacity.theMovieDBExampleMVP.view.fragments.MovieDetailFragment;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Presenter that controls flow of movie details screen
 */
public class MovieDetailsPresenter extends BasePresenter<IMovieDetailsView>
        implements IMovieDetailsPresenter, LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Key for movie bundle values
     */
    private static final String EXTRA_MOVIE = "EXTRA_MOVIE";

    /**
     * Movie's loader ID
     */
    private static final int MOVIE_LOADER = 1;

    /**
     * Review's loader ID
     */
    private static final int REVIEW_LOADER = 2;

    /**
     * Video's loader ID
     */
    private static final int VIDEO_LOADER = 3;

    /**
     * Movie whos details will be rendered
     */
    protected Movie mMovie;

    public MovieDetailsPresenter() {
        super();
        PopMovies.getInstance().graph().inject(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadData(Bundle bundle) {
        if (bundle == null) return;

        mMovie = bundle.getParcelable(MovieDetailFragment.EXTRA_MOVIE_KEY);
    }

    /**
     * Invokes loader manager for content provider
     */
    @Override
    public void onActivityCreated() {
        LoaderManager manager = ((AppCompatActivity) getView().getContext())
                .getSupportLoaderManager();
        manager.initLoader(MOVIE_LOADER, null, this);
        manager.initLoader(VIDEO_LOADER, null, this);
        manager.initLoader(REVIEW_LOADER, null, this);
    }

    /**
     * Loads movie's details if screen is created
     */
    @Override
    protected void onViewCreated() {
        loadMovieFromDatabase();
    }

    /**
     * Initiates loading of a movie from a DB
     */
    protected void loadMovieFromDatabase() {
        LoaderManager manager = ((AppCompatActivity) getView().getContext())
                .getSupportLoaderManager();

        manager.restartLoader(MOVIE_LOADER, null, this);
        manager.restartLoader(VIDEO_LOADER, null, this);
        manager.restartLoader(REVIEW_LOADER, null, this);
    }

    /**
     * Loads reviews for a movie from API
     */
    protected void loadReviews() {
        if (getView() == null || mMovie == null) return;

        Observable<Response<ReviewsResponse>> retrofitObservable
                = mApi.getMovieReviews(mMovie.getId(), Constants.API_KEY);
        makeApiCall(retrofitObservable);
    }

    /**
     * Loads videos for a movie from API
     */
    protected void loadVideos() {
        if (getView() == null || mMovie == null) return;

        Observable<Response<VideosResponse>> retrofitObservable
                = mApi.getMovieVideos(mMovie.getId(), Constants.API_KEY);
        makeApiCall(retrofitObservable);
    }

    /**
     * Callback for successful API response. Updates the movie with the response and triggers
     * rendering in the end.
     * @param apiResponse API response object
     * @param <T> Response type
     */
    @Override
    protected <T extends BaseResponse> void onApiResponse(T apiResponse) {
        if (apiResponse instanceof ReviewsResponse) {
            mMovie.setReviews(apiResponse.getResults());
            loadVideos();
            return;
        } else if (apiResponse instanceof VideosResponse) {
            mMovie.setVideos(apiResponse.getResults());

        }

        renderMovie();
    }

    /**
     * Renders a movie to the view
     */
    protected void renderMovie() {
        if (!isViewAttached()) return;

        getView().hideProgressBar();
        getView().renderMovie(mMovie);
    }

    /**
     * Loads movie's videos and reviews if they were not loaded.
     * Renders movie if screen is restored
     */
    @Override
    protected void onViewRestored() {
        if (mMovie != null && mMovie.getReviews() == null) {
            loadReviews();
            return;
        }

        if (mMovie != null && mMovie.getVideos() == null) {
            loadVideos();
        }

        renderMovie();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void restoreData(Bundle savedInstanceState) {
        mMovie = savedInstanceState.getParcelable(EXTRA_MOVIE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(EXTRA_MOVIE, mMovie);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onScreenResumed() {
    }

    /**
     * Triggered when usr clicks on favorite button. Adds or removes movie from favorites
     * database depending on previus state.
     */
    @Override
    public void onFavoritesClicked() {
        if (getView() == null || mMovie == null) return;

        if(mMovie.isFavorite()) {
            removeMovieFromFavorites();
            return;
        }

        addMovieToFavorites();
    }

    /**
     * Removes movie, reviews, videos and poster from local storage
     */
    protected void removeMovieFromFavorites() {
        Observable<Boolean> deleteDataObservable = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> observer) {
                if (getView() == null || mMovie == null) return;

                deleteMovieFromDb();
                Utils.deleteImageFromDisk(getView().getContext(), mMovie.getId());
                if (observer.isUnsubscribed()) return;
                observer.onNext(true);
                observer.onCompleted();
            }
        });

        deleteDataObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean o) {
                        if (mMovie == null || getView() == null) return;

                        mMovie.setFavorite(false);
                        renderMovie();
                    }
                });
    }

    /**
     * Deletes movie, reviews and trailers from DB
     */
    protected void deleteMovieFromDb() {
        ContentResolver contentResolver = getView().getContext().getContentResolver();
        contentResolver.delete(MovieContract.MovieEntry.CONTENT_URI,
                MovieContract.MovieEntry._ID + " = ?",
                new String[]{mMovie.getId()});
        contentResolver.delete(MovieContract.ReviewEntry.CONTENT_URI,
                MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{mMovie.getId()});
        contentResolver.delete(MovieContract.VideoEntry.CONTENT_URI,
                MovieContract.VideoEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{mMovie.getId()});
    }

    /**
     * Adds movie, reviews, trailers and poster to local storage
     */
    protected void addMovieToFavorites() {
        Observable<Boolean> addDataObservable = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> observer) {
                if (getView() == null || mMovie == null) return;

                saveMovieToDb();
                Utils.saveBitmapToJpeg(getView().getContext(),
                        mMovie.getId(),
                        getView().getMovieBitmap());

                if (observer.isUnsubscribed()) return;
                observer.onNext(true);
                observer.onCompleted();
            }
        });


        addDataObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean b) {
                        if (mMovie == null || getView() == null) return;

                        mMovie.setFavorite(true);
                        renderMovie();
                    }
                });
    }

    /**
     * Saves movie, reviews and trailers to DB
     */
    protected void saveMovieToDb() {
        ContentResolver contentResolver = getView().getContext().getContentResolver();
        ContentValues movieContentValues = mMovie.getContentValues();
        contentResolver.insert(MovieContract.MovieEntry.CONTENT_URI, movieContentValues);
        ContentValues[] videosContentValues = mMovie.getVideosContentValues();
        if (videosContentValues != null) {
            contentResolver.bulkInsert(MovieContract.VideoEntry.CONTENT_URI, videosContentValues);
        }

        ContentValues[] reviewsContentValues = mMovie.getReviewsContentValues();
        if (reviewsContentValues != null) {
            contentResolver.bulkInsert(MovieContract.ReviewEntry.CONTENT_URI, reviewsContentValues);
        }
    }

    /**
     * Returns first trailer URL if possible
     * @return URL or <code>null</code> if there are no trailers
     */
    @Override
    public String getTrailerUrl() {
        if (doesMovieHasTrailers()) {
            return mMovie.getVideos().get(0).getYoutubeUrl();
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doesMovieHasTrailers() {
        return mMovie != null && mMovie.getVideos() != null && mMovie.getVideos().size() > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder;
        Uri uri;
        String[] columns;
        String selection;
        String movieId = mMovie == null ? "-1" : mMovie.getId();
        String[] selectionArgs = new String[] { movieId };
        switch (id) {
            case MOVIE_LOADER:
                sortOrder = MovieContract.MovieEntry._ID + " ASC";
                uri = MovieContract.MovieEntry.CONTENT_URI;
                columns = MovieMapper.MOVIE_COLUMNS;
                selection = MovieContract.MovieEntry._ID + " = ?";
                break;
            case VIDEO_LOADER:
                sortOrder = MovieContract.VideoEntry._ID + " ASC";
                uri = MovieContract.VideoEntry.CONTENT_URI;
                columns = VideoMapper.VIDEO_COLUMNS;
                selection = MovieContract.VideoEntry.COLUMN_MOVIE_ID + " = ?";
                break;
            default:
                sortOrder = MovieContract.ReviewEntry._ID + " ASC";
                uri = MovieContract.ReviewEntry.CONTENT_URI;
                columns = ReviewMapper.REVIEW_COLUMNS;
                selection = MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " = ?";
                break;
        }

        return new CursorLoader(getView().getContext(),
                uri,
                columns,
                selection,
                selectionArgs,
                sortOrder);
    }

    /**
     * Adds reviews and trailers to the movie if in favorites database and renders the data.
     * If movie is not in favorites then API calls are initiated
     * @param loader Loader
     * @param cursor Cursor
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (getView() == null) return;

        if (mMovie == null) {
            renderMovie();
            return;
        }

        cursor.moveToFirst();
        switch (loader.getId()) {
            case MOVIE_LOADER:
                Movie movie = null;
                while(!cursor.isAfterLast()) {
                    movie = new Movie(cursor);
                    cursor.moveToNext();
                }

                if (movie == null) {
                    loadReviews();
                    return;
                }

                mMovie.setFavorite(true);
                break;
            case REVIEW_LOADER:
                List<Review> reviews = new ArrayList<>();
                while (!cursor.isAfterLast()) {
                    Review review = new Review(cursor);
                    reviews.add(review);
                    cursor.moveToNext();
                }

                mMovie.setReviews(reviews);
                break;
            case VIDEO_LOADER:
                List<Video> videos = new ArrayList<>();
                while (!cursor.isAfterLast()) {
                    Video video = new Video(cursor);
                    videos.add(video);
                    cursor.moveToNext();
                }

                mMovie.setVideos(videos);
                break;
        }

        renderMovie();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
