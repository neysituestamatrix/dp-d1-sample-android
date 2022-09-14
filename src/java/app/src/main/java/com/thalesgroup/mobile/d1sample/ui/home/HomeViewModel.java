package com.thalesgroup.mobile.d1sample.ui.home;

/*
 * Copyright © 2022 THALES. All rights reserved.
 */

import com.thalesgroup.gemalto.d1.D1Exception;
import com.thalesgroup.gemalto.d1.D1Task;
import com.thalesgroup.mobile.d1sample.sdk.D1Helper;
import com.thalesgroup.mobile.d1sample.ui.base.BaseViewModel;

/**
 * ViewModel for the virtual card list.
 */
public class HomeViewModel extends BaseViewModel {
    /**
     * Logs out.
     */
    public void logout() {
        D1Helper.getInstance().logout(new D1Task.Callback<Void>() {
            @Override
            public void onSuccess(final Void data) {
                mIsOperationSuccesfull.postValue(true);
            }

            @Override
            public void onError(final D1Exception exception) {
                // ignore error as session can be expired, but show error message
                mIsOperationSuccesfull.postValue(true);
                mErrorMessage.postValue(exception.getLocalizedMessage());
            }
        });
    }

    /**
     * Retrieves the D1 SDK versions.
     *
     * @return D1 SDK versions.
     */
    public String getLibVersions() {
        return D1Helper.getInstance().getLibVersions();
    }
}
