/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.model.beans.mappers;

import android.os.Parcel;
import android.support.annotation.NonNull;

import java.util.List;

import popmovies.udacity.theMovieDBExampleMVP.model.beans.Gallery;
import popmovies.udacity.theMovieDBExampleMVP.model.beans.Movie;
import popmovies.udacity.theMovieDBExampleMVP.presenter.Constants;

/**
 * Defines set of static methods used to create and map {@link Gallery}
 */
public class GalleryMapper {

    /**
     * Writes {@link Gallery} object to {@link Parcel}
     * @param parcel Parcel object
     * @param gallery Gallery object
     */
    public static void writeToParcel(@NonNull Parcel parcel, @NonNull Gallery gallery) {
        parcel.writeByte(gallery.hasMore() ? Constants.BOOLEAN_TRUE : Constants.BOOLEAN_FALSE);
        parcel.writeInt(gallery.getGalleryType().ordinal());
        parcel.writeInt(gallery.getLastLoadedPage());
        parcel.writeTypedList(gallery.getMovies());
    }

    /**
     * Constructs {@link Gallery} from {@link Parcel}
     * @param parcel Parcel object
     * @return Gallery object constructed from {@link Parcel}
     */
    public static Gallery constructFromParcel(@NonNull Parcel parcel) {
        Gallery gallery = new Gallery();
        gallery.setHasMore(parcel.readByte() == Constants.BOOLEAN_TRUE);
        gallery.setGalleryType(
                Gallery.GalleryType.fromOrdinal(parcel.readInt())
        );
        gallery.setLastLoadedPage(parcel.readInt());

        List<Movie> movies = parcel.createTypedArrayList(Movie.CREATOR);
        gallery.addMovies(movies);
        return gallery;
    }
}
