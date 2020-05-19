/**
 * Copyright (C) 2016 The Android Open Source Project
 * Developed by ssinss, downloaded from https://gist.github.com/ssinss/e06f12ef66c51252563e
 */

package popmovies.udacity.theMovieDBExampleMVP.view.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Autofit {@link RecyclerView} that acts as a grid with
 * allowed definition of column width
 */
public class AutofitRecyclerView extends RecyclerView {

    /**
     * Defined column width
     */
    protected float mColumnWidth;

    /**
     * Layout manager
     */
    protected GridLayoutManager mManager;

    public AutofitRecyclerView(Context context) {
        super(context);
        init(context, null);
    }

    public AutofitRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AutofitRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    /**
     * Loads column width from given attributes if defined
     * @param context Context
     * @param attrs Attribute set
     */
    protected void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            int[] attrsArray = {
                    android.R.attr.columnWidth
            };
            TypedArray array = context.obtainStyledAttributes(
                    attrs, attrsArray);
            mColumnWidth = array.getDimensionPixelSize(0, -1);
            array.recycle();
        }

        mManager = new GridLayoutManager(getContext(), 1);
        mManager.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(mManager);
    }

    /**
     * Measures the dimensions of recycler view, computing number of columns per width of screen
     * @param widthSpec Defined width spec
     * @param heightSpec Defined height spec
     */
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        if (mColumnWidth == 0) return;

        int spanCount = Math.max(1, (int) (getMeasuredWidth() / mColumnWidth));
        mManager.setSpanCount(spanCount);
    }
}
