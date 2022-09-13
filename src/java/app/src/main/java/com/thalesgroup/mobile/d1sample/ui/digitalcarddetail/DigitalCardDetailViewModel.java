package com.thalesgroup.mobile.d1sample.ui.digitalcarddetail;/*
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

import android.view.View;

import com.thalesgroup.gemalto.d1.D1Exception;
import com.thalesgroup.gemalto.d1.D1Task;
import com.thalesgroup.gemalto.d1.card.DigitalCard;
import com.thalesgroup.gemalto.d1.card.State;
import com.thalesgroup.mobile.d1sample.sdk.D1Helper;
import com.thalesgroup.mobile.d1sample.ui.base.BaseViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

/**
 * ViewModel for digital card detail.
 */
public class DigitalCardDetailViewModel extends BaseViewModel {

    public MutableLiveData<String> mCardState = new MutableLiveData<>();
    public MutableLiveData<String> mScheme = new MutableLiveData<>();
    public MutableLiveData<String> mLast4Digits = new MutableLiveData<>();
    public MutableLiveData<String> mExpr = new MutableLiveData<>();
    public MutableLiveData<String> mDeviceName = new MutableLiveData<>();
    public MutableLiveData<String> mDeviceType = new MutableLiveData<>();
    public MutableLiveData<String> mWalletId = new MutableLiveData<>();
    public MutableLiveData<String> mWalletName = new MutableLiveData<>();
    public MutableLiveData<Integer> mSuspendButtonVisibility = new MutableLiveData<>();
    public MutableLiveData<Integer> mResumeButtonVisibility = new MutableLiveData<>();

    private DigitalCard mDigitalCard;

    /**
     * Retrieves the digital card details.
     *
     * @param cardId        Card ID.
     * @param digitalCardId Digital card ID.
     */
    public void getDigitalCardDetails(@NonNull final String cardId, @NonNull final String digitalCardId) {
        mSuspendButtonVisibility.postValue(View.GONE);
        mResumeButtonVisibility.postValue(View.GONE);

        D1Helper.getInstance().getDigitalCardListD1Push(cardId, new D1Task.Callback<List<DigitalCard>>() {
            @Override
            public void onSuccess(final List<DigitalCard> data) {
                mIsOperationSuccesfull.postValue(true);

                for (final DigitalCard digitalCard : data) {
                    if (digitalCard.getCardID().equals(digitalCardId)) {
                        mScheme.postValue("Scheme: " + digitalCard.getScheme().toString());
                        mLast4Digits.postValue("**** **** **** " + digitalCard.getLast4());
                        mCardState.postValue("Card State: " + digitalCard.getState().toString());
                        mDeviceName.postValue("Device Name: " + digitalCard.getDeviceName());
                        mDeviceType.postValue("Device Type: " + digitalCard.getDeviceType());
                        mWalletId.postValue("Walled Id: " + digitalCard.getWalletID());
                        mWalletName.postValue("Wallet Name: " + digitalCard.getWalletName());

                        mDigitalCard = digitalCard;

                        if (digitalCard.getState() == State.ACTIVE) {
                            mSuspendButtonVisibility.postValue(View.VISIBLE);
                            mResumeButtonVisibility.postValue(View.GONE);
                        } else {
                            mSuspendButtonVisibility.postValue(View.GONE);
                            mResumeButtonVisibility.postValue(View.VISIBLE);
                        }

                        break;
                    }
                }
            }

            @Override
            public void onError(final D1Exception exception) {
                mErrorMessage.postValue(exception.getLocalizedMessage());
            }
        });
    }

    /**
     * Suspends the digital card.
     *
     * @param cardId Card ID.
     */
    public void suspendDigitalCard(@NonNull final String cardId) {
        D1Helper.getInstance().suspendDigitalCard(cardId, mDigitalCard, new D1Task.Callback<Boolean>() {
            @Override
            public void onSuccess(final Boolean data) {
                // retrieve new card state
                getDigitalCardDetails(cardId, mDigitalCard.getCardID());
            }

            @Override
            public void onError(final D1Exception exception) {
                mErrorMessage.postValue(exception.getLocalizedMessage());
            }
        });
    }

    /**
     * Resumes the digital card.
     *
     * @param cardId Card ID.
     */
    public void resumeDigitalCard(@NonNull final String cardId) {
        D1Helper.getInstance().resumeDigitalCard(cardId, mDigitalCard, new D1Task.Callback<Boolean>() {
            @Override
            public void onSuccess(final Boolean data) {
                // retrieve new card state
                getDigitalCardDetails(cardId, mDigitalCard.getCardID());
            }

            @Override
            public void onError(final D1Exception exception) {
                mErrorMessage.postValue(exception.getLocalizedMessage());
            }
        });
    }

    /**
     * Deletes the digital card.
     *
     * @param cardId Card ID.
     */
    public void deleteDigitalCard(@NonNull final String cardId) {
        D1Helper.getInstance().suspendDigitalCard(cardId, mDigitalCard, new D1Task.Callback<Boolean>() {
            @Override
            public void onSuccess(final Boolean data) {
                mIsOperationSuccesfull.postValue(true);

                mCardState.setValue(State.DELETED.toString());
            }

            @Override
            public void onError(final D1Exception exception) {
                mErrorMessage.postValue(exception.getLocalizedMessage());
            }
        });
    }
}
