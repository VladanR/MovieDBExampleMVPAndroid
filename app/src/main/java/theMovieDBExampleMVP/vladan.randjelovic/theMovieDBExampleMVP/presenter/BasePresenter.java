/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.presenter;

import android.os.Bundle;
import android.support.annotation.CallSuper;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import popmovies.udacity.theMovieDBExampleMVP.model.api.ApiQueryObservable;
import popmovies.udacity.theMovieDBExampleMVP.model.api.MoviesApi;
import popmovies.udacity.theMovieDBExampleMVP.model.api.response.BaseResponse;
import popmovies.udacity.theMovieDBExampleMVP.presenter.interfaces.presenter.IPresenter;
import popmovies.udacity.theMovieDBExampleMVP.presenter.interfaces.view.IView;
import retrofit2.Response;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Abstract representation of a presenter and its base methods
 */
public abstract class BasePresenter<V extends IView> implements IPresenter {

    /**
     * View that it holds
     */
    private V mView;

    /**
     * Retrofit API
     */
    @Inject protected MoviesApi mApi;

    /**
     * List of active subscriptions
     */
    private List<Subscription> mSubscriptions;

    /**
     * Defines if state has just been restored or not
     */
    private boolean mRestoredState;

    public BasePresenter() {
        mRestoredState = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends IView> void setView(T view) {
        mView = (V) view;
    }

    /**
     * Checks if view is attached to presenter
     * @return <b>true</b> if view is available, <b>false</b> otherwise
     */
    protected boolean isViewAttached() {
        return mView != null;
    }

    /**
     * {@inheritDoc}
     *
     * Handles as well logic after restoring state of a screen.
     */
    @Override
    final public void onScreenCreated() {
        if (!isViewAttached()) return;

        if (mRestoredState) {
            mRestoredState = false;
            onViewRestored();
        } else {
            onViewCreated();
        }
    }

    /**
     * Invoked when view has been restored
     */
    protected abstract void onViewRestored();

    /**
     * Invoked when view has been created
     */
    protected abstract void onViewCreated();

    /**
     * Restores state of the screen
     * @param savedInstanceState Bundle data used to restore instance state
     */
    @Override
    final public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (null == savedInstanceState || !isViewAttached()) return;

        mRestoredState = true;
        restoreData(savedInstanceState);
    }

    /**
     * Restores data of the presenter
     * @param savedInstanceState Bundle data used to restore instance state
     */
    protected abstract void restoreData(Bundle savedInstanceState);

    /**
     * Returns view interface that holds presenter
     * @return View interface
     */
    protected V getView() {
        return mView;
    }

    /**
     * Clears the holder variables and stops API calls. Calling <code>super.onScreenDestroy()</code>
     * is required if you are overriding the method.
     */
    @CallSuper
    @Override
    public void onScreenDestroy() {
        mView = null;
        mApi = null;
        if (mSubscriptions == null) return;

        for (Subscription subscription : mSubscriptions) {
            if (subscription == null || subscription.isUnsubscribed()) continue;

            subscription.unsubscribe();
        }
    }

    /**
     * Adds {@link Subscription} to the list of active subscriptions
     * @param subscription {@link Subscription} to add
     */
    private void addSubscription(Subscription subscription) {
        if (mSubscriptions == null) {
            mSubscriptions = new ArrayList<>();
        }

        mSubscriptions.add(subscription);
    }

    /**
     * Makes an API call using {@link ApiQueryObservable}, if error was encountered invokes
     * view specific methods.
     * @param observable Retrofit observable
     * @param <T> Type of response
     */
    protected <T extends BaseResponse> void makeApiCall(Observable<Response<T>> observable) {
        if (!isViewAttached()) return;

        Subscription subscription = ApiQueryObservable.createObservable(observable)
                .map()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<T>() {
                    @Override
                    public void call(T apiResponse) {
                        onApiResponse(apiResponse);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.hideProgressBar();
                        if (throwable instanceof UnknownHostException) {
                            mView.showNoInternetConnection();
                        } else {
                            mView.showServerErrorMessage();
                        }
                    }
                });
        addSubscription(subscription);
    }

    /**
     * Invoked when API returns response
     * @param apiResponse API response object
     * @param <T> Type of response object
     */
    protected abstract <T extends BaseResponse> void onApiResponse(T apiResponse);
}
