/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.model.api;

import android.support.annotation.NonNull;

import popmovies.udacity.theMovieDBExampleMVP.model.api.response.BaseResponse;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * An {@link Observable} of {@link BaseResponse} which offers query-specific convenience operators.
 */
public class ApiQueryObservable<T> extends Observable<Response<T>> {

    /**
     * Creates new instance of {@link ApiQueryObservable} from Retrofit observable
     * @param observable Observable to subscribe
     * @return {@link ApiQueryObservable} instance
     */
    public static final <T> ApiQueryObservable<T> createObservable(final Observable<Response<T>> observable) {
        return new ApiQueryObservable(new OnSubscribe<Response>() {
            @Override
            public void call(Subscriber<? super Response> subscriber) {
                observable.unsafeSubscribe(subscriber);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    protected ApiQueryObservable(OnSubscribe<Response<T>> f) {
        super(f);
    }

    /**
     * Given an error handler passes any encountered errors to it.
     * @return Operator that maps the result and as well sends any errors caused by API
     * communication.
     */
    @NonNull
    public final Observable<T> map() {
        return lift(new ApiQueryOperator<T>());
    }
}
