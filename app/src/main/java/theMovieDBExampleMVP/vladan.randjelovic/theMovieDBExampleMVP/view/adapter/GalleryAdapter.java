/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import popmovies.udacity.theMovieDBExampleMVP.R;
import popmovies.udacity.theMovieDBExampleMVP.model.beans.Gallery;
import popmovies.udacity.theMovieDBExampleMVP.model.beans.Movie;
import popmovies.udacity.theMovieDBExampleMVP.view.Utils;

/**
 * Adapter for gallery that renders movies thumbnails
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder>{

    public interface OnMovieClickedListener {
        /**
         * Invoked when user clicks on a movie
         * @param movie Movie that has been clicked on
         */
        void onMovieClicked(Movie movie);

        /**
         * Checks if app is run on tablet
         * @return <code>true</code> if app is run on tablet, <code>false</code> otherwise
         */
        boolean isTabletMode();
    }

    /**
     * Gallery items that will be rendered
     */
    protected Gallery mGallery;

    /**
     * Selected item
     */
    protected int mSelectedItem;

    /**
     * Creates an instance of an adapter
     * @param gallery Gallery that will be rendered
     */
    public GalleryAdapter(Gallery gallery) {
        mSelectedItem = -1;
        setHasStableIds(true);
        replaceItems(gallery);
    }

    /**
     * Replaces gallery in the adapter and renders new one
     * @param gallery Gallery to render
     */
    public void replaceItems(Gallery gallery) {
        mGallery = gallery;
        if (mGallery.getMovies() == null || mGallery.getMovies().size() < mSelectedItem) {
            mSelectedItem = -1;
        }

        notifyDataSetChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_gallery, parent, false);
        return new ViewHolder(view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(GalleryAdapter.ViewHolder holder, int position) {
        holder.render(mGallery.getMovies().get(position), position == mSelectedItem);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return (mGallery == null || mGallery.getMovies() == null) ?
                0 :
                mGallery.getMovies().size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Gallery thumbnail
         */
        @BindView(R.id.thumbnail)
        protected ImageView mThumnbail;

        /**
         * Top selector
         */
        @BindView(R.id.top_selector)
        protected View mTopSelector;

        /**
         * Bottom selector
         */
        @BindView(R.id.bottom_selector)
        protected View mBottomSelector;

        /**
         * Creates view holder for a view
         * @param itemView View
         */
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        /**
         * On click of a movie invokes callback to notify of it
         * @param v Clicked thumbnail view
         */
        @OnClick(R.id.thumbnail)
        void onMovieClicked(View v) {
            Movie movie = (Movie) v.getTag();
            OnMovieClickedListener listener = ((OnMovieClickedListener) v.getContext());
            listener.onMovieClicked(movie);

            if (listener.isTabletMode()) {
                mSelectedItem = getLayoutPosition();
                //Selected element in recycler view is usually handled with notifyItemChanged
                //before and after selected item position is changed statement, but
                //Picasso has a bug that when you do that it often loads wrong images
                //into wrong views. Not best solution to call this but at least provides normal
                //UX to the user. Should be changed once the Picasso has this bug fixed in
                //production mode.
                notifyDataSetChanged();
            }
        }

        /**
         * Renders a movie in grid cell
         * @param movie Movie to render
         * @param selected Defines if current element is selected
         */
        public void render(Movie movie, boolean selected) {
            if (movie == null) return;

            loadThumbnail(movie);
            mThumnbail.setTag(movie);
            itemView.setContentDescription(movie.getTitle());
            int visibility = selected ? View.VISIBLE : View.INVISIBLE;
            mBottomSelector.setVisibility(visibility);
            mTopSelector.setVisibility(visibility);
        }

        /**
         * Loads movie poster from disk or network
         * @param movie Movie
         */
        void loadThumbnail(Movie movie) {
            String filePath = Utils.getFullImagePathIfExists(
                    mThumnbail.getContext(), movie.getId());
            Picasso picasso = Picasso.with(mThumnbail.getContext().getApplicationContext());
            picasso.setLoggingEnabled(true);
            RequestCreator creator;
            if (filePath == null) {
                creator = picasso
                        .load(movie.getMoviePosterFullUrl());
            } else {
                creator = picasso
                        .load(new File(filePath));
            }

            creator.fit()
                    .centerCrop()
                    .noFade()
                    .error(R.drawable.ic_stop)
                    .placeholder(R.drawable.ic_action_refresh)
                    .into(mThumnbail);

            mThumnbail.setContentDescription(movie.getTitle());
        }
    }
}
