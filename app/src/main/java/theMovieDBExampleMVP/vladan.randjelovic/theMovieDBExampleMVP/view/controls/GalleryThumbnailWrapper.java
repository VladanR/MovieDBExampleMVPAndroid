/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.view.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Gallery thumbnail wrapper view control that keeps aspect ratio
 * of the thumbnail from the API.
 */
public class GalleryThumbnailWrapper extends RelativeLayout {

    /**
     * Optimal height based on API image height
     */
    private static final float THUMBNAIL_OPTIMAL_HEIGHT = 277;

    /**
     * Optimal width based on API image width
     */
    private static final float THUMBNAIL_OPTIMAL_WIDTH = 185;

    /**
     * {@inheritDoc}
     */
    public GalleryThumbnailWrapper(Context context) {
        super(context);
    }

    /**
     * {@inheritDoc}
     */
    public GalleryThumbnailWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * {@inheritDoc}
     */
    public GalleryThumbnailWrapper(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * {@inheritDoc}
     *
     * When finished with measuring thumbnail image view wrapper will be in same
     * aspect ratio as API image
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = Math.round((width * THUMBNAIL_OPTIMAL_HEIGHT) / THUMBNAIL_OPTIMAL_WIDTH);

        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }
}