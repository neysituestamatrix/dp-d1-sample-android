/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.virtualcarddetail;

import android.app.Activity;
import android.view.View;

import com.thalesgroup.gemalto.d1.D1Exception;
import com.thalesgroup.gemalto.d1.D1Task;
import com.thalesgroup.gemalto.d1.card.CardDetails;
import com.thalesgroup.gemalto.d1.card.CardDigitizationState;
import com.thalesgroup.gemalto.d1.card.CardMetadata;
import com.thalesgroup.gemalto.d1.card.State;
import com.thalesgroup.mobile.d1sample.sdk.D1Helper;
import com.thalesgroup.mobile.d1sample.ui.base.BaseViewModel;

import java.nio.charset.StandardCharsets;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

/**
 * ViewModel for virtual card detail.
 */
public class VirtualCardDetailViewModel extends BaseViewModel {

    public MutableLiveData<String> mPan = new MutableLiveData<>();
    public MutableLiveData<String> mExpr = new MutableLiveData<>();
    public MutableLiveData<String> mName = new MutableLiveData<>();
    public MutableLiveData<String> mCvv = new MutableLiveData<>();
    public MutableLiveData<String> mCardState = new MutableLiveData<>();
    public MutableLiveData<Integer> mSuspendButtonVisibility = new MutableLiveData<>();
    public MutableLiveData<Integer> mResumeButtonVisibility = new MutableLiveData<>();
    public MutableLiveData<Integer> mAddCardButtonVisibility = new MutableLiveData<>();
    public MutableLiveData<Boolean> mAddCardButtonEnabled = new MutableLiveData<>(Boolean.FALSE);
    public MutableLiveData<Boolean> mEnableNFCPayment = new MutableLiveData<>(Boolean.FALSE);
    public MutableLiveData<Boolean> mShowDigitalCard = new MutableLiveData<>(Boolean.FALSE);
    public MutableLiveData<Boolean> mDigitizationStartedOk = new MutableLiveData<>(Boolean.FALSE);
    public MutableLiveData<Boolean> mDigitizationFinishedOk = new MutableLiveData<>(Boolean.FALSE);
    public MutableLiveData<CardDigitizationState> mCardDigitizationStateD1Pay
            = new MutableLiveData<>(CardDigitizationState.NOT_DIGITIZED);

    private String mMaskedPan;

    /**
     * Retrieves the virtual card details.
     *
     * @param cardId Card ID.
     */
    public void getCardMetadata(@NonNull final String cardId) {
        D1Helper.getInstance().getCardMetadata(cardId, new D1Task.Callback<CardMetadata>() {
            @Override
            public void onSuccess(final CardMetadata data) {
                mIsOperationSuccesfull.postValue(true);

                mMaskedPan = "**** **** **** " + data.getLast4Pan();
                mPan.postValue("**** **** **** " + data.getLast4Pan());
                mExpr.postValue(data.getExpiryDate());
                mCardState.postValue(data.getState().toString());

                if (data.getState() == State.ACTIVE) {
                    mSuspendButtonVisibility.postValue(View.VISIBLE);
                    mResumeButtonVisibility.postValue(View.GONE);
                } else {
                    mSuspendButtonVisibility.postValue(View.GONE);
                    mResumeButtonVisibility.postValue(View.VISIBLE);
                }

                extractImageResources(data);
            }

            @Override
            public void onError(final D1Exception exception) {
                mErrorMessage.postValue(exception.getLocalizedMessage());
            }
        });
    }

    /**
     * Retrieves the protected card details.
     *
     * @param cardId Card ID.
     */
    public void getCardDetails(@NonNull final String cardId) {
        D1Helper.getInstance().getCardDetails(cardId, new D1Task.Callback<CardDetails>() {
            @Override
            public void onSuccess(final CardDetails data) {
                mIsOperationSuccesfull.postValue(true);

                mName.postValue(new String(data.getCardHolderName(), StandardCharsets.UTF_8));
                mPan.postValue(new String(data.getPan(), StandardCharsets.UTF_8));
                mCvv.postValue(new String(data.getCvv(), StandardCharsets.UTF_8));

                data.wipe();
            }

            @Override
            public void onError(final D1Exception exception) {
                mErrorMessage.postValue(exception.getLocalizedMessage());
            }
        });
    }

    /**
     * Clears the card details.
     */
    public void hideCardDetails() {
        mName.postValue("");
        mPan.postValue(mMaskedPan);
        mCvv.postValue("");

        mIsOperationSuccesfull.postValue(true);
    }

    /**
     * Checks if card is digitized.
     *
     * @param cardId Card ID.
     */
    public void isCardDigitized(@NonNull final String cardId) {
        D1Helper.getInstance().getCardDigitizationState(cardId, new D1Task.Callback<CardDigitizationState>() {
            @Override
            public void onSuccess(final CardDigitizationState cardDigitizationState) {
                switch (cardDigitizationState) {
                    case NOT_DIGITIZED:
                        mAddCardButtonEnabled.postValue(true);
                        break;
                    case DIGITIZED:
                    case PENDING_IDV:
                    case DIGITIZATION_IN_PROGRESS:
                        mAddCardButtonEnabled.postValue(false);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onError(@NonNull final D1Exception exception) {
                mErrorMessage.postValue(exception.getLocalizedMessage());
            }
        });
    }

    public void isCardDigitizedNfc(@NonNull final String cardId) {
        D1Helper.getInstance().getD1PayDigitizationState(cardId, new D1Task.Callback<CardDigitizationState>() {
            @Override
            public void onSuccess(final CardDigitizationState data) {
                mCardDigitizationStateD1Pay.postValue(data);

                switch (data) {
                    case NOT_DIGITIZED:
                        mEnableNFCPayment.postValue(true);
                        mShowDigitalCard.postValue(false);
                        break;
                    case DIGITIZED:
                    case PENDING_IDV:
                        mEnableNFCPayment.postValue(false);
                        mShowDigitalCard.postValue(true);
                        break;
                    case DIGITIZATION_IN_PROGRESS:
                        mEnableNFCPayment.postValue(false);
                        mEnableNFCPayment.postValue(false);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onError(@NonNull final D1Exception exception) {
                mErrorMessage.postValue(exception.getLocalizedMessage());
            }
        });
    }

    /**
     * Digitizes the card to wallet.
     *
     * @param cardId Card ID.
     */
    public void digitizeCard(@NonNull final String cardId, @NonNull final Activity activity) {
        D1Helper.getInstance().digitizeCard(cardId, activity, new D1Task.Callback<Object>() {
            @Override
            public void onSuccess(final Object data) {
                mIsOperationSuccesfull.postValue(true);
                isCardDigitized(cardId); // get card state
            }

            @Override
            public void onError(final D1Exception exception) {
                mErrorMessage.postValue(exception.getLocalizedMessage());
            }
        });
    }

    /**
     * Digitizes the card to application.
     *
     * @param cardId Card ID.
     */
    public void digitizeCardD1Pay(@NonNull final String cardId) {
        // Digitization 1. Register CardDataChangeListener
        D1Helper.getInstance().registerCardDataChangeListenerD1Pay((card, state) -> {
            if (card != null && state == State.ACTIVE) {
                mDigitizationFinishedOk.postValue(true);

                isCardDigitizedNfc(card);
                D1Helper.getInstance().unregisterCardDataChangeListenerD1Pay();
            }
        });

        // Digitization 2. Start card digitization.
        D1Helper.getInstance().digitizeD1PayCard(cardId, new D1Task.Callback<Void>() {
            @Override
            public void onSuccess(final Void data) {
                // Digitization 3. Data will be updated through Push Notification.
                mEnableNFCPayment.postValue(false); // disable button
                mDigitizationStartedOk.postValue(true);
            }

            @Override
            public void onError(@NonNull final D1Exception exception) {
                mErrorMessage.postValue(exception.getLocalizedMessage());
            }
        });
    }
}
