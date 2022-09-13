package com.thalesgroup.mobile.d1sample.ui.d1paydigitalcard;/*
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
import com.thalesgroup.gemalto.d1.card.CardMetadata;
import com.thalesgroup.gemalto.d1.card.State;
import com.thalesgroup.gemalto.d1.d1pay.D1PayDigitalCard;
import com.thalesgroup.mobile.d1sample.sdk.D1Helper;
import com.thalesgroup.mobile.d1sample.ui.base.BaseViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

/**
 * ViewModel for the virtual card list.
 */
public class D1PayDigitalCardViewModel extends BaseViewModel {
    public MutableLiveData<String> mCardId = new MutableLiveData<>();
    public MutableLiveData<String> mCardState = new MutableLiveData<>();
    public MutableLiveData<String> mLast4Pan = new MutableLiveData<>();
    public MutableLiveData<String> mExpr = new MutableLiveData<>();
    public MutableLiveData<String> mIsDefault = new MutableLiveData<>();
    public MutableLiveData<Boolean> mIsDeleteCardSuccess = new MutableLiveData<>(Boolean.FALSE);
    public MutableLiveData<Integer> mSuspendButtonVisibility = new MutableLiveData<>(View.GONE);
    public MutableLiveData<Integer> mResumeButtonVisibility = new MutableLiveData<>(View.GONE);
    public MutableLiveData<Integer> mSetDefaultButtonVisibility = new MutableLiveData<>(View.GONE);
    public MutableLiveData<Integer> mUnsetDefaultButtonVisibility = new MutableLiveData<>(View.GONE);

    /**
     * Gets the D1Pay digital card details.
     */
    public void getD1PayDigitalCard(@NonNull final String cardId) {
        D1Helper.getInstance().getDigitalCardD1Pay(cardId, new D1Task.Callback<D1PayDigitalCard>() {
            @Override
            public void onSuccess(final D1PayDigitalCard d1PayDigitalCard) {
                if (d1PayDigitalCard == null) {
                    mErrorMessage.postValue("No D1Pay Digital card available.");
                } else {
                    mCardId.postValue(d1PayDigitalCard.getCardID());
                    mCardState.postValue(d1PayDigitalCard.getState().toString());
                    mLast4Pan.postValue("**** **** **** " + d1PayDigitalCard.getLast4());
                    mExpr.postValue("EXPR: " + d1PayDigitalCard.getExpiryDate());
                    mIsDefault.postValue(d1PayDigitalCard.isDefaultCard() ? " YES" : " NO");
                    mIsOperationSuccesfull.postValue(true);

                    if (d1PayDigitalCard.isDefaultCard()) {
                        mSetDefaultButtonVisibility.postValue(View.GONE);
                        mUnsetDefaultButtonVisibility.postValue(View.VISIBLE);
                    } else {
                        mSetDefaultButtonVisibility.postValue(View.VISIBLE);
                        mUnsetDefaultButtonVisibility.postValue(View.GONE);
                    }

                    if (d1PayDigitalCard.getState() == State.ACTIVE) {
                        mSuspendButtonVisibility.postValue(View.VISIBLE);
                        mResumeButtonVisibility.postValue(View.GONE);
                    } else if (d1PayDigitalCard.getState() == State.INACTIVE) {
                        mSuspendButtonVisibility.postValue(View.GONE);
                        mResumeButtonVisibility.postValue(View.VISIBLE);
                    } else {
                        mSuspendButtonVisibility.postValue(View.GONE);
                        mResumeButtonVisibility.postValue(View.GONE);
                    }
                }
            }

            @Override
            public void onError(@NonNull final D1Exception exception) {
                mErrorMessage.postValue(exception.getLocalizedMessage());
            }
        });
    }

    /**
     * Retrieves the card image.
     *
     * @param cardId Card ID.
     */
    public void getCardImages(@NonNull final String cardId) {
        D1Helper.getInstance().getCardMetadata(cardId, new D1Task.Callback<CardMetadata>() {
            @Override
            public void onSuccess(final CardMetadata data) {
                extractImageResources(data);
            }

            @Override
            public void onError(final D1Exception exception) {
                // ignore error
            }
        });
    }

    /**
     * Suspends the D1Pay digital card.
     *
     * @param cardId Card ID.
     */
    public void suspendD1PayDigitalCard(@NonNull final String cardId) {
        D1Helper.getInstance().getDigitalCardD1Pay(cardId, new D1Task.Callback<D1PayDigitalCard>() {
            @Override
            public void onSuccess(final D1PayDigitalCard digitalCard) {
                D1Helper.getInstance().suspendD1PayDigitalCard(cardId, digitalCard, new D1Task.Callback<Boolean>() {
                    @Override
                    public void onSuccess(final Boolean aBoolean) {
                        // get most recent state
                        getD1PayDigitalCard(cardId);
                    }

                    @Override
                    public void onError(@NonNull final D1Exception exception) {
                        mErrorMessage.postValue(exception.getLocalizedMessage());
                    }
                });
            }

            @Override
            public void onError(@NonNull final D1Exception exception) {
                mErrorMessage.postValue(exception.getLocalizedMessage());
            }
        });
    }

    /**
     * Resumes the D1Pay digital card.
     *
     * @param cardId Card ID.
     */
    public void resumeD1PayDigitalCard(@NonNull final String cardId) {
        D1Helper.getInstance().getDigitalCardD1Pay(cardId, new D1Task.Callback<D1PayDigitalCard>() {
            @Override
            public void onSuccess(final D1PayDigitalCard digitalCard) {
                D1Helper.getInstance().resumeD1PayDigitalCard(cardId, digitalCard, new D1Task.Callback<Boolean>() {
                    @Override
                    public void onSuccess(final Boolean aBoolean) {
                        // get most recent state
                        getD1PayDigitalCard(cardId);
                    }

                    @Override
                    public void onError(@NonNull final D1Exception exception) {
                        mErrorMessage.postValue(exception.getLocalizedMessage());
                    }
                });
            }

            @Override
            public void onError(@NonNull final D1Exception exception) {
                mErrorMessage.postValue(exception.getLocalizedMessage());
            }
        });
    }

    /**
     * Deletes the D1Pay digital card.
     *
     * @param cardId Card ID.
     */
    public void deleteD1PayDigitalCard(@NonNull final String cardId) {
        D1Helper.getInstance().getDigitalCardD1Pay(cardId, new D1Task.Callback<D1PayDigitalCard>() {
            @Override
            public void onSuccess(final D1PayDigitalCard digitalCard) {
                D1Helper.getInstance().deleteD1PayDigitalCard(cardId, digitalCard, new D1Task.Callback<Boolean>() {
                    @Override
                    public void onSuccess(final Boolean aBoolean) {
                        mIsDeleteCardSuccess.postValue(aBoolean);
                    }

                    @Override
                    public void onError(@NonNull final D1Exception exception) {
                        mErrorMessage.postValue(exception.getLocalizedMessage());
                    }
                });
            }

            @Override
            public void onError(@NonNull final D1Exception exception) {
                mErrorMessage.postValue(exception.getLocalizedMessage());
            }
        });
    }

    /**
     * Sets the D1Pay didital card as the default payment card.
     *
     * @param cardId Card ID.
     */
    public void setDefaultCard(@NonNull final String cardId) {
        D1Helper.getInstance().setDefaultD1PayDigitalCard(cardId, new D1Task.Callback<Void>() {
            @Override
            public void onSuccess(final Void unused) {
                // get most recent state
                getD1PayDigitalCard(cardId);
            }

            @Override
            public void onError(@NonNull final D1Exception exception) {
                mErrorMessage.postValue(exception.getLocalizedMessage());
            }
        });
    }

    /**
     * Unsets the D1Pay didital card as the default payment card.
     *
     * @param cardId Card ID.
     */
    public void unSetDefaultCard(@NonNull final String cardId) {
        D1Helper.getInstance().unsetDefaultD1PayDigitalCard(new D1Task.Callback<Void>() {
            @Override
            public void onSuccess(final Void unused) {
                // get most recent state
                getD1PayDigitalCard(cardId);
            }

            @Override
            public void onError(@NonNull final D1Exception exception) {
                mErrorMessage.postValue(exception.getLocalizedMessage());
            }
        });
    }
}