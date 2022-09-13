package com.thalesgroup.mobile.d1sample.ui.login;

import android.os.Looper;

import com.thalesgroup.gemalto.d1.D1Exception;
import com.thalesgroup.gemalto.d1.D1Task;
import com.thalesgroup.mobile.d1sample.jwt.JWTEncoder;
import com.thalesgroup.mobile.d1sample.jwt.Tenant;
import com.thalesgroup.mobile.d1sample.sdk.Configuration;
import com.thalesgroup.mobile.d1sample.sdk.D1Helper;
import com.thalesgroup.mobile.d1sample.ui.base.BaseViewModel;

import java.nio.charset.StandardCharsets;

/**
 * Login ViewModel.
 */
public class LoginViewModel extends BaseViewModel {
    /**
     * Logs in to D1.
     */
    public void login() {
        // TODO: retrieve the access token
        final String jwt = JWTEncoder.generateA2BAn(Tenant.SANDBOX, Configuration.CONSUMER_ID);

        D1Helper.getInstance().login(jwt.getBytes(StandardCharsets.UTF_8), new D1Task.Callback<Void>() {
            @Override
            public void onSuccess(final Void data) {
                mIsOperationSuccesfull.postValue(true);
            }

            @Override
            public void onError(final D1Exception exception) {
                mErrorMessage.postValue(exception.getLocalizedMessage());
            }
        });
    }
}