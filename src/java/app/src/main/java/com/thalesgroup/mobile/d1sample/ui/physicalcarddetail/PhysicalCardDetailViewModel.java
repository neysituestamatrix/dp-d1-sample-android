/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.physicalcarddetail;

import com.thalesgroup.gemalto.d1.D1Exception;
import com.thalesgroup.gemalto.d1.D1Task;
import com.thalesgroup.gemalto.d1.PINDisplayTextView;
import com.thalesgroup.gemalto.d1.SecureEditText;
import com.thalesgroup.gemalto.d1.card.CardActivationMethod;
import com.thalesgroup.mobile.d1sample.sdk.D1Helper;
import com.thalesgroup.mobile.d1sample.ui.base.BaseViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

/**
 * ViewModel for virtual card detail.
 */
public class PhysicalCardDetailViewModel extends BaseViewModel {

    public MutableLiveData<String> mCardActivationMethod = new MutableLiveData<>(CardActivationMethod.NOTHING.toString());

    /**
     * Activates the physical card.
     *
     * @param cardId   Card ID.
     * @param secureTextEdit Secure text edit, where the code entered.
     */
    public void activatePhysicalCard(@NonNull final String cardId, @NonNull final SecureEditText secureTextEdit) {
        D1Helper.getInstance().activatePhysicalCard(cardId, secureTextEdit, new D1Task.Callback<Void>() {
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

    /**
     * Retrieves the PIN of the physical card.
     *
     * @param cardId   Card ID.
     * @param pinDisplayTextView PINDisplayTextView where the PIN is displayed.
     */
    public void getPhysicalCardPin(@NonNull final String cardId, @NonNull final PINDisplayTextView pinDisplayTextView) {
        D1Helper.getInstance().getPhysicalCardPin(cardId, pinDisplayTextView, new D1Task.Callback<Void>() {
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

    /**
     * Retrieves card activation method.
     *
     * @param cardId   Card ID.
     */
    public void getActivationMethod(@NonNull final String cardId) {
        D1Helper.getInstance().getActivationMethod(cardId, new D1Task.Callback<CardActivationMethod>() {
            @Override
            public void onSuccess(final CardActivationMethod data) {
                mIsOperationSuccesfull.postValue(true);
                mCardActivationMethod.postValue(data.toString());
            }

            @Override
            public void onError(final D1Exception exception) {
                mErrorMessage.postValue(exception.getLocalizedMessage());
            }
        });
    }
}
