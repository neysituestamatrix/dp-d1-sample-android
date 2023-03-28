/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.thalesgroup.gemalto.d1.D1Exception;
import com.thalesgroup.gemalto.d1.D1Task;
import com.thalesgroup.gemalto.d1.validation.BuildConfig;
import com.thalesgroup.mobile.d1sample.sdk.Configuration;
import com.thalesgroup.mobile.d1sample.sdk.D1Helper;
import com.thalesgroup.mobile.d1sample.util.Constants;

import java.util.List;

/**
 * Main Application class.
 */
public class D1App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("APP", "Launching");

        Configuration.loadConfigurationFromAssets(this);

        // if D1Pay is in scope, we need to init D1 there in order to support immediate background payment
        if (BuildConfig.FLAVOR.equals(Constants.PRODUCT_FLAVOR_D1_PAY)) {
            configureD1PaySDK();
        }
    }

    /**
     * Configures D1Pay SDK.
     */
    public void configureD1PaySDK() {
        D1Helper.getInstance().configureD1Pay(
                Configuration.consumerId,
                this,
                new D1Task.ConfigCallback<Void>() {
                    @Override
                    public void onSuccess(final Void data) {
                        // no need to notify UI.
                    }

                    @Override
                    public void onError(@NonNull final List<D1Exception> exceptions) {
                        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(D1App.this, exceptions.get(0).getLocalizedMessage(), Toast.LENGTH_LONG).show());
                    }
                });
    }
}
