/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.splash;

import android.app.Activity;
import android.content.Context;

import com.thalesgroup.gemalto.d1.D1Exception;
import com.thalesgroup.gemalto.d1.D1Task;
import com.thalesgroup.gemalto.d1.d1pay.ContactlessTransactionListener;
import com.thalesgroup.mobile.d1sample.sdk.Configuration;
import com.thalesgroup.mobile.d1sample.sdk.D1Helper;
import com.thalesgroup.mobile.d1sample.ui.base.BaseViewModel;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Splash ViewModel.
 */
public class SplashViewModel extends BaseViewModel {
    /**
     * Configures D1 SDK.
     */
    public void configure(@NonNull final Context applicationContext,
                          @NonNull final Activity activity,
                          @NonNull final ContactlessTransactionListener contactlessTransactionListener) {
        // TODO: retrieve consumer id
        D1Helper.getInstance().configure(Configuration.CONSUMER_ID,
                                         activity,
                                         applicationContext,
                                         contactlessTransactionListener,
                                         new D1Task.ConfigCallback<Void>() {
                                             @Override
                                             public void onSuccess(final Void data) {
                                                 mIsOperationSuccesfull.postValue(true);
                                             }

                                             @Override
                                             public void onError(@NonNull final List<D1Exception> exceptions) {
                                                 if (!exceptions.isEmpty()) {
                                                     mErrorMessage.postValue(exceptions.get(0).getLocalizedMessage());
                                                 }
                                             }
                                         });
    }
}