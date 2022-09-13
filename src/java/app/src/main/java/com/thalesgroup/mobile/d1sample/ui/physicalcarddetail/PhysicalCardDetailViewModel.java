package com.thalesgroup.mobile.d1sample.ui.physicalcarddetail;/*
 * MIT License
 *
 * Copyright (c) 2020 Thales DIS
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
