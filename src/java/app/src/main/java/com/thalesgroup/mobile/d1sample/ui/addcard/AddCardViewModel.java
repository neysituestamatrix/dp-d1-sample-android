/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.addcard;

import com.thalesgroup.gemalto.d1.D1Exception;
import com.thalesgroup.gemalto.d1.D1Task;
import com.thalesgroup.mobile.d1sample.sdk.D1Helper;
import com.thalesgroup.mobile.d1sample.ui.base.BaseViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

/**
 * Add card ViewModel.
 */
public class AddCardViewModel extends BaseViewModel {
    public MutableLiveData<String> mName = new MutableLiveData<>();
    public MutableLiveData<String> mAddress1 = new MutableLiveData<>();
    public MutableLiveData<String> mAddress2 = new MutableLiveData<>();
    public MutableLiveData<String> mCountryCode = new MutableLiveData<>();
    public MutableLiveData<String> mLocality = new MutableLiveData<>();
    public MutableLiveData<String> mAdministrativeArea = new MutableLiveData<>();
    public MutableLiveData<String> mPhoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> mPostalCode = new MutableLiveData<>();

    /**
     * Digitizes the card.
     *
     * @param cardId Card ID.
     */
    @SuppressWarnings("PMD.CyclomaticComplexity") // Not much we can do about this
    public void digitizeCard(@NonNull final String cardId) {
        if (mName.getValue() == null || mAddress1.getValue() == null || mAddress2.getValue() == null
            || mCountryCode.getValue() == null || mLocality.getValue() == null || mAdministrativeArea.getValue() == null
            || mPhoneNumber.getValue() == null || mPostalCode.getValue() == null || mName.getValue().isEmpty() || mAddress1
                    .getValue().isEmpty() || mAddress2.getValue().isEmpty() || mCountryCode.getValue().isEmpty() || mLocality
                    .getValue().isEmpty() || mPhoneNumber.getValue().isEmpty() || mLocality.getValue().isEmpty()
            || mAdministrativeArea.getValue().isEmpty()) {
            mErrorMessage.postValue("Error: Missing values.");
            return;
        }

        D1Helper.getInstance().digitizeCard(cardId, new D1Task.Callback<Void>() {
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
