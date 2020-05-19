/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.model.api;

import java.io.IOException;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.OnErrorThrowable;

/**
 * Operator that handles API communication returning response.
 */
public class ApiQueryOperator<T> implements Observable.Operator<T, Response<T>> {

    public ApiQueryOperator() {
    }

    /**
     * Maps result calls to the subscriber but as well handles the error states if any
     * occurred during communication with API
     * @param subscriber {@link Subscriber} that listens for result
     * @return {@link Subscriber} that can be used for communication with API
     */
    @Override
    public Subscriber<Response<T>> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<Response<T>>(subscriber) {
            @Override public void onNext(Response<T> response) {
                if (response.isSuccessful()) {
                    if (subscriber.isUnsubscribed()) return;

                    subscriber.onNext(response.body());
                    return;
                }

                try {
                    Throwable throwable = OnErrorThrowable
                            .addValueAsLastCause(new Exception(), response.errorBody().string());
                    onError(throwable);
                } catch (IOException e) {
                    onError(e);
                }
            }

            @Override public void onCompleted() {
                if (subscriber.isUnsubscribed()) return;
                subscriber.onCompleted();
            }

            @Override public void onError(Throwable e) {
                if (subscriber.isUnsubscribed()) return;
                subscriber.onError(e);
            }
        };
    }
}
