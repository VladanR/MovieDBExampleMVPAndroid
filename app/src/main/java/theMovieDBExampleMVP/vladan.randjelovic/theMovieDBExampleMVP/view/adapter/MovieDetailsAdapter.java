/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import popmovies.udacity.theMovieDBExampleMVP.R;
import popmovies.udacity.theMovieDBExampleMVP.model.beans.Movie;
import popmovies.udacity.theMovieDBExampleMVP.model.beans.Review;
import popmovies.udacity.theMovieDBExampleMVP.model.beans.Video;
import popmovies.udacity.theMovieDBExampleMVP.view.Utils;

/**
 * Adapter that renders movie details
 */
public class MovieDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * Interface that notifies click on favorite button for a movie
     */
    public interface OnFavoriteButtonClick {
        /**
         * Invoked when user clicks on a favorite button for a given movie
         */
        void onFavoriteButtonClick();
    }

    /**
     * View tyep for movie details
     */
    private static final byte VIEW_TYPE_MOVIE_DETAILS = 0;

    /**
     * View type for review section header
     */
    private static final byte VIEW_TYPE_REVIEW_HEADER = 1;

    /**
     * View type for video section header
     */
    private static final byte VIEW_TYPE_VIDEO_HEADER = 2;

    /**
     * View type for trailer
     */
    private static final byte VIEW_TYPE_VIDEO = 3;

    /**
     * View type for review
     */
    private static final byte VIEW_TYPE_REVIEW = 4;

    /**
     * View type for placeholder
     */
    private static final byte VIEW_TYPE_PLACEHOLDER = 5;

    /**
     * First video starts from position two, first position are movie details, second
     * section header
     */
    private static final byte FIRST_VIDEO_OFFSET_POSITION = 2;

    /**
     * First review has offset of 1 comparing to his title in position
     */
    private static final byte FIRST_REVIEW_OFFSET = 1;

    /**
     * Movie that will be rendered
     */
    protected Movie mMovie;

    /**
     * Listener that handles click on favorite button
     */
    protected OnFavoriteButtonClick mListener;

    /**
     * Poster bitmap
     */
    protected Bitmap mMovieBitmap;

    /**
     * Creates an instance of an adapter
     * @param movie Movie that will be rendered
     * @param listener Listener that handles logic when favorite button is clicked
     */
    public MovieDetailsAdapter(Movie movie, OnFavoriteButtonClick listener) {
        mListener = listener;
        replaceMovie(movie);
    }

    /**
     * Replaces movie in the adapter and renders new one
     * @param movie Movie to render
     */
    public void replaceMovie(Movie movie) {
        mMovie = movie;
        notifyDataSetChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEW_TYPE_MOVIE_DETAILS:
                view = layoutInflater.inflate(R.layout.list_item_movie_details, parent, false);
                return new MovieDetailsViewHolder(view);
            case VIEW_TYPE_REVIEW:
                view = layoutInflater.inflate(R.layout.list_item_movie_review, parent, false);
                return new ReviewViewHolder(view);
            case VIEW_TYPE_REVIEW_HEADER:
            case VIEW_TYPE_VIDEO_HEADER:
                view = layoutInflater
                        .inflate(R.layout.list_item_movie_section_header, parent, false);
                return new SectionHeaderViewHolder(view);
            case VIEW_TYPE_VIDEO:
                view = layoutInflater.inflate(R.layout.list_item_movie_trailer, parent, false);
                return new TrailerViewHolder(view);
            default:
                view = layoutInflater.inflate(
                        R.layout.list_item_movie_details_placeholder, parent, false);
                return new PlaceholderViewHolder(view);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemViewType(int position) {
        if (shouldShowPlaceholder()) {
            return VIEW_TYPE_PLACEHOLDER;
        }

        if (shouldShowMovieDetails(position)) {
            return VIEW_TYPE_MOVIE_DETAILS;
        }

        if (shouldShowTrailersSectionHeader(position)) {
            return VIEW_TYPE_VIDEO_HEADER;
        }

        if (shouldShowTrailer(position)) {
            return VIEW_TYPE_VIDEO;
        }

        if (shouldShowReviewSectionHeader(position)) {
            return VIEW_TYPE_REVIEW_HEADER;
        }

        return VIEW_TYPE_REVIEW;
    }

    /**
     * Checks if placeholder should be shown
     * @return <code>true</code> if placeholder should be shown, <code>false</code> otherwise
     */
    boolean shouldShowPlaceholder() {
        return mMovie == null;
    }

    /**
     * Checks if movie details should be shown on given position
     * @param position Position to render
     * @return <code>true</code> if movie details should be rendered, <code>false</code> otherwise
     */
    boolean shouldShowMovieDetails(int position) {
        return position == 0 && mMovie != null;
    }

    /**
     * Checks if trailers section header should be shown on given position
     * @param position Position to render
     * @return <code>true</code> if trailers section header
     * should be rendered, <code>false</code> otherwise
     */
    boolean shouldShowTrailersSectionHeader(int position) {
        return position == 1 && mMovie.getVideos() != null && mMovie.getVideos().size() > 0;
    }

    /**
     * Checks if trailer should be shown on given position
     * @param position Position to render
     * @return <code>true</code> if trailer should be rendered, <code>false</code> otherwise
     */
    boolean shouldShowTrailer(int position) {
        int videosSize = mMovie == null || mMovie.getVideos() == null ?
                0 : mMovie.getVideos().size();
        int bottomTrailerDelimiter = FIRST_VIDEO_OFFSET_POSITION + videosSize;
        return position > 1 && position < bottomTrailerDelimiter;
    }

    /**
     * Checks if reviews section header should be shown on given position
     * @param position Position to render
     * @return <code>true</code> if reviews section
     * header should be rendered, <code>false</code> otherwise
     */
    boolean shouldShowReviewSectionHeader(int position) {
        int reviewsSize = mMovie == null || mMovie.getReviews() == null ?
                0 : mMovie.getReviews().size();
        if (mMovie.getVideos() == null || mMovie.getVideos().size() == 0) {
            return position == 1 && mMovie.getReviews() != null && reviewsSize > 0;
        } else {
            return (position == FIRST_VIDEO_OFFSET_POSITION + mMovie.getVideos().size())
                    && mMovie.getReviews() != null && reviewsSize > 0;
        }
    }

    /**
     * Returns rendered bitmap for a poster
     * @return {@link Bitmap} of a poster
     */
    public Bitmap getMovieBitmap() {
        return mMovieBitmap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_REVIEW_HEADER:
                ((SectionHeaderViewHolder) holder).renderReviewsTitle();
                break;
            case VIEW_TYPE_REVIEW:
                ((ReviewViewHolder) holder).render(getReviewByPosition(position));
                break;
            case VIEW_TYPE_VIDEO:
                ((TrailerViewHolder) holder).render(getVideoByPosition(position));
                break;
            case VIEW_TYPE_MOVIE_DETAILS:
                ((MovieDetailsViewHolder) holder).render(mMovie);
                break;
            case VIEW_TYPE_VIDEO_HEADER:
                ((SectionHeaderViewHolder) holder).renderTrailersTitle();
                break;
            default:
                break;
        }
    }

    /**
     * Returns review to render on given position
     * @param position Position to render
     * @return {@link Review} to render
     */
    Review getReviewByPosition(int position) {
        if (mMovie.getVideos() == null || mMovie.getVideos().size() == 0) {
            return mMovie.getReviews().get(position - FIRST_VIDEO_OFFSET_POSITION);
        } else {
            int reviewPosition = position -
                    FIRST_VIDEO_OFFSET_POSITION - mMovie.getVideos().size() - FIRST_REVIEW_OFFSET;
            return mMovie.getReviews().get(reviewPosition);
        }
    }

    /**
     * Returns video to render on given position
     * @param position Position to render
     * @return {@link Video} to render
     */
    Video getVideoByPosition(int position) {
        return mMovie.getVideos().get(position - FIRST_VIDEO_OFFSET_POSITION);
    }

    /**
     * Returns number of rows to render, including headers of videos and reviews sections.
     */
    @Override
    public int getItemCount() {
        if (mMovie == null) return 1;

        int rowCount = 1;
        if (mMovie.getVideos() != null && mMovie.getVideos().size() > 0) {
            rowCount += mMovie.getVideos().size() + 1;
        }

        if (mMovie.getReviews() != null && mMovie.getReviews().size() > 0) {
            rowCount += mMovie.getReviews().size() + 1;
        }

        return rowCount;
    }

    protected class MovieDetailsViewHolder extends RecyclerView.ViewHolder {

        /**
         * Movie title
         */
        @BindView(R.id.movie_detail_title) protected TextView mTitle;

        /**
         * Movie information
         */
        @BindView(R.id.movie_detail_info) protected TextView mMovieInfo;

        /**
         * Thumbnail
         */
        @BindView(R.id.movie_detail_thumbnail) protected ImageView mThumnbail;

        /**
         * Overview
         */
        @BindView(R.id.movie_detail_overview) protected TextView mOverview;

        /**
         * Favorites button
         */
        @BindView(R.id.favorites) protected Button mFavoritesBtn;

        /**
         * Creates view holder for a view
         * @param itemView View
         */
        public MovieDetailsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        /**
         * Renders a movie details
         * @param movie Movie to render
         */
        public void render(Movie movie) {
            if (movie == null) return;

            Context context = mThumnbail.getContext();
            mTitle.setText(movie.getTitle());
            mOverview.setText(movie.getPlotOverview());

            String releaseDateString = TextUtils.isEmpty(movie.getReleaseDate()) ?
                    context.getString(R.string.date_not_defined) :
                    movie.getReleaseDate();

            String ratingString = movie.getUserRating() == 0 ?
                    context.getString(R.string.rating_not_defined) :
                    String.valueOf(movie.getUserRating());
            String movieInfo = String.format(
                    context.getString(R.string.format_movie_details),
                    releaseDateString,
                    ratingString);

            mMovieInfo.setText(movieInfo);
            loadMoviePoster(movie);

            if (movie.isFavorite()) {
                mFavoritesBtn.setText(R.string.remove_from_favorites);
            } else {
                mFavoritesBtn.setText(R.string.add_to_favorites);
            }
        }

        /**
         * Loads poster of a movie in an image view
         * @param movie Movie to render the poster for
         */
        void loadMoviePoster(Movie movie) {
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    mMovieBitmap = bitmap;
                    mThumnbail.setImageBitmap(mMovieBitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    mThumnbail.setImageDrawable(errorDrawable);
                    mMovieBitmap = null;
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    mThumnbail.setImageDrawable(placeHolderDrawable);
                    mMovieBitmap = null;
                }
            };

            String filePath = Utils.getFullImagePathIfExists(
                    mThumnbail.getContext(), movie.getId());
            if (filePath == null) {
                Picasso.with(mThumnbail.getContext().getApplicationContext())
                        .load(movie.getMoviePosterFullUrl())
                        .error(R.drawable.ic_stop)
                        .placeholder(R.drawable.ic_action_refresh)
                        .into(target);
            } else {
                Picasso.with(mThumnbail.getContext().getApplicationContext())
                        .load(new File(filePath))
                        .fit()
                        .centerCrop()
                        .error(R.drawable.ic_stop)
                        .placeholder(R.drawable.ic_action_refresh)
                        .into(mThumnbail);
            }
        }

        /**
         * Invoked when user clicks on favorite button
         */
        @OnClick(R.id.favorites)
        protected void onFavoritesClicked() {
            if (mListener == null) return;

            mListener.onFavoriteButtonClick();
        }
    }

    protected static class SectionHeaderViewHolder extends RecyclerView.ViewHolder {

        /**
         * Section title
         */
        @BindView(R.id.header_title) protected TextView mTitle;

        /**
         * Creates view holder for a view
         * @param itemView View
         */
        public SectionHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        /**
         * Renders title for reviews section
         */
        public void renderReviewsTitle() {
            mTitle.setText(R.string.reviews_title);
        }

        /**
         * Renders title for trailers section
         */
        public void renderTrailersTitle() {
            mTitle.setText(R.string.trailers_title);
        }
    }

    protected static class ReviewViewHolder extends RecyclerView.ViewHolder {

        /**
         * Review author
         */
        @BindView(R.id.review_author) protected TextView mAuthor;

        /**
         * Review content
         */
        @BindView(R.id.review) protected TextView mContent;

        /**
         * Creates view holder for a view
         * @param itemView View
         */
        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        /**
         * Renders review
         * @param review Review to render
         */
        public void render(Review review) {
            mAuthor.setText(review.getAuthor());
            mContent.setText(review.getContent());
        }
    }

    protected static class TrailerViewHolder extends RecyclerView.ViewHolder {

        /**
         * Trailer name
         */
        @BindView(R.id.trailer_name) protected TextView mTrailerName;

        /**
         * Creates view holder for a view
         * @param itemView View
         */
        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        /**
         * Renders trailer
         * @param trailer Trailer to render
         */
        public void render(Video trailer) {
            mTrailerName.setText(trailer.getName());
            mTrailerName.setTag(trailer.getYoutubeUrl());
        }

        /**
         * Plays trailer if clicked
         * @param v View of trailer
         */
        @OnClick(R.id.trailer_name)
        void onTrailerClicked(View v) {
            String url = (String) mTrailerName.getTag();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            v.getContext().startActivity(i);
        }
    }

    protected static class PlaceholderViewHolder extends RecyclerView.ViewHolder {

        public PlaceholderViewHolder(View itemView) {
            super(itemView);
        }
    }
}