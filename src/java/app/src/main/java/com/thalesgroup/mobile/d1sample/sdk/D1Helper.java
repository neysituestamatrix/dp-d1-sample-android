/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.thalesgroup.gemalto.d1.CardPINUI;
import com.thalesgroup.gemalto.d1.ConfigParams;
import com.thalesgroup.gemalto.d1.D1Exception;
import com.thalesgroup.gemalto.d1.D1Params;
import com.thalesgroup.gemalto.d1.D1Task;
import com.thalesgroup.gemalto.d1.EntryUI;
import com.thalesgroup.gemalto.d1.PINDisplayTextView;
import com.thalesgroup.gemalto.d1.PushResponseKey;
import com.thalesgroup.gemalto.d1.SecureEditText;
import com.thalesgroup.gemalto.d1.card.CardAction;
import com.thalesgroup.gemalto.d1.card.CardActivationMethod;
import com.thalesgroup.gemalto.d1.card.CardDataChangedListener;
import com.thalesgroup.gemalto.d1.card.CardDetails;
import com.thalesgroup.gemalto.d1.card.CardDigitizationState;
import com.thalesgroup.gemalto.d1.card.CardMetadata;
import com.thalesgroup.gemalto.d1.card.DigitalCard;
import com.thalesgroup.gemalto.d1.card.OEMPayType;
import com.thalesgroup.gemalto.d1.d1pay.D1PayConfigParams;
import com.thalesgroup.gemalto.d1.d1pay.D1PayDataChangedListener;
import com.thalesgroup.gemalto.d1.d1pay.D1PayDigitalCard;
import com.thalesgroup.gemalto.d1.d1pay.DeviceAuthenticationCallback;
import com.thalesgroup.gemalto.d1.d1pay.TransactionHistory;
import com.thalesgroup.mobile.d1sample.sdk.payment.D1PayTransactionListener;
import com.thalesgroup.mobile.d1sample.util.HexUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

/**
 * Helper class for D1 SDK.
 */
public final class D1Helper {
    private static final D1Helper INSTANCE = new D1Helper();
    private final MutableLiveData<SdkConfigState> mSdkConfigurationState
            = new MutableLiveData(SdkConfigState.NOT_CONFIGURED);
    private D1Task mD1Task;
    private D1PayTransactionListener mD1PayTransactionListener;

    private D1Helper() {
        // private constructor
    }

    /**
     * Retrieves the singleton instance of {@code D1Helper}.
     *
     * @return Singleton instance of {@code D1Helper}.
     */
    @NonNull
    public static D1Helper getInstance() {
        return INSTANCE;
    }

    /**
     * Configures the D1 SDK.
     *
     * @param consumerId Consumer ID.
     * @param activity   Activity.
     * @param callback   Callback.
     */
    public void configure(@NonNull final String consumerId,
                          @NonNull final Activity activity,
                          @NonNull final D1Task.ConfigCallback<Void> callback) {
        // D1Core config.
        mD1Task = new D1Task.Builder().setContext(activity.getApplicationContext())
                                      .setD1ServiceURL(Configuration.d1ServiceUrl).setIssuerID(Configuration.issuerId)
                                      .setD1ServiceRSAExponent(HexUtil.hexStringToByteArray(Configuration.d1ServiceRsaExponent))
                                      .setD1ServiceRSAModulus(HexUtil.hexStringToByteArray(Configuration.d1ServiceRsaModulus))
                                      .setDigitalCardURL(Configuration.digitalCardUrl).build();

        final D1Params coreConfig = ConfigParams.buildConfigCore(consumerId);
        final D1Params cardConfig = ConfigParams.buildConfigCard(activity, OEMPayType.GOOGLE_PAY, null);

        // D1Pay config.
        final D1PayConfigParams d1PayConfigParams = D1PayConfigParams.getInstance();
        d1PayConfigParams.setContactlessTransactionListener(
                mD1PayTransactionListener = new D1PayTransactionListener(activity.getApplicationContext()));
        d1PayConfigParams.setReplenishAuthenticationUIStrings("Replenishment Title",
                                                              "Replenishment Subtitle",
                                                              "Replenishment Description",
                                                              "Cancel");

        final D1Params[] d1Params = new D1Params[]{coreConfig, cardConfig, d1PayConfigParams};

        synchronized (INSTANCE) {
            mSdkConfigurationState.postValue(SdkConfigState.WORKING);
        }

        mD1Task.configure(new D1Task.ConfigCallback<Void>() {
            @Override
            public void onSuccess(final Void unused) {
                synchronized (INSTANCE) {
                    mSdkConfigurationState.postValue(SdkConfigState.CONFIGURED);
                }

                getCurrentPushToken();

                callback.onSuccess(unused);
            }

            @Override
            public void onError(@NonNull final List<D1Exception> list) {
                synchronized (INSTANCE) {
                    mSdkConfigurationState.postValue(SdkConfigState.ERROR);
                }

                callback.onError(list);
            }
        }, d1Params);
    }

    /**
     * Configures the D1 SDK.
     *
     * @param applicationContext Application context.
     * @param callback           Callback.
     */
    public void configureD1Pay(@NonNull final Context applicationContext,
                               @NonNull final D1Task.ConfigCallback<Void> callback) {
        // D1Core config.
        mD1Task = new D1Task.Builder().setContext(applicationContext).setD1ServiceURL(Configuration.d1ServiceUrl)
                                      .setIssuerID(Configuration.issuerId)
                                      .setD1ServiceRSAExponent(HexUtil.hexStringToByteArray(Configuration.d1ServiceRsaExponent))
                                      .setD1ServiceRSAModulus(HexUtil.hexStringToByteArray(Configuration.d1ServiceRsaModulus))
                                      .setDigitalCardURL(Configuration.digitalCardUrl).build();

        // D1Pay config.
        final D1PayConfigParams d1PayConfigParams = D1PayConfigParams.getInstance();
        d1PayConfigParams.setContactlessTransactionListener(
                mD1PayTransactionListener = new D1PayTransactionListener(applicationContext));
        d1PayConfigParams.setReplenishAuthenticationUIStrings("Replenishment Title",
                                                              "Replenishment Subtitle",
                                                              "Replenishment Description",
                                                              "Cancel");
        final D1Params[] d1Params = new D1Params[]{d1PayConfigParams};

        mD1Task.configure(new D1Task.ConfigCallback<Void>() {
            @Override
            public void onSuccess(final Void unused) {
                callback.onSuccess(unused);
            }

            @Override
            public void onError(@NonNull final List<D1Exception> list) {
                callback.onError(list);
            }
        }, d1Params);
    }

    /**
     * Logs in.
     *
     * @param issuerToken Issuer token.
     * @param callback    Callback.
     */
    public void login(@NonNull final byte[] issuerToken, @NonNull final D1Task.Callback<Void> callback) {
        getD1Task().login(issuerToken, callback);
    }

    /**
     * Logs out.
     *
     * @param callback Callback.
     */
    public void logout(@NonNull final D1Task.Callback<Void> callback) {
        getD1Task().logout(callback);
    }

    public void handleResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        getD1Task().handleCardResult(requestCode, resultCode, data);
    }

    /**
     * Retrieves the card meta data.
     *
     * @param cardId   Card ID.
     * @param callback Callback.
     */
    public void getCardMetadata(@NonNull final String cardId, @NonNull final D1Task.Callback<CardMetadata> callback) {
        getD1Task().getCardMetadata(cardId, callback);
    }


    /**
     * Retrieves the card meta data.
     *
     * @param cardId   Card ID.
     * @param callback Callback.
     */
    public void getD1PayCardMetadata(@NonNull final String cardId, @NonNull final D1Task.Callback<CardMetadata> callback) {
        getD1Task().getCardMetadata(cardId, callback);
    }

    /**
     * Retrieves the card meta data.
     *
     * @param cardId   Card ID.
     * @param callback Callback.
     */
    public void getCardDetails(@NonNull final String cardId, @NonNull final D1Task.Callback<CardDetails> callback) {
        getD1Task().getCardDetails(cardId, callback);
    }

    /**
     * Checks if card is digitized.
     *
     * @param cardId   Card ID.
     * @param callback Callback.
     */
    public void getCardDigitizationState(@NonNull final String cardId,
                                         @NonNull final D1Task.Callback<CardDigitizationState> callback) {
        getD1Task().getD1PushWallet().getCardDigitizationState(cardId, OEMPayType.GOOGLE_PAY, callback);
    }

    /**
     * Digitizes the card.
     *
     * @param cardId   Card ID.
     * @param callback Callback.
     */
    public void digitizeCard(@NonNull final String cardId, @NonNull final Activity activity, @NonNull final D1Task.Callback<Object> callback) {
        getD1Task().getD1PushWallet().addDigitalCardToOEM(cardId, OEMPayType.GOOGLE_PAY, activity, callback);
    }

    /**
     * Activates the digital card.
     *
     * @param cardId   Card ID.
     * @param callback Callback.
     */
    public void activateDigitalCard(@NonNull final String cardId, @NonNull final D1Task.Callback<Void> callback) {
        getD1Task().getD1PushWallet().activateDigitalCard(cardId, OEMPayType.GOOGLE_PAY, callback);
    }

    /**
     * Activates the physical card.
     *
     * @param cardId         Card ID.
     * @param secureTextEdit Secure text edit, where the code entered.
     * @param callback       Callback.
     */
    public void activatePhysicalCard(@NonNull final String cardId,
                                     @NonNull final SecureEditText secureTextEdit,
                                     @NonNull final D1Task.Callback<Void> callback) {
        final EntryUI entryUI = new EntryUI(secureTextEdit);
        getD1Task().activatePhysicalCard(cardId, entryUI, callback);
    }

    /**
     * Retrieves the PIN of the physical card.
     *
     * @param cardId             Card ID.
     * @param pinDisplayTextView PINDisplayTextView where the PIN is displayed.
     * @param callback           Callback.
     */
    public void getPhysicalCardPin(@NonNull final String cardId,
                                   @NonNull final PINDisplayTextView pinDisplayTextView,
                                   @NonNull final D1Task.Callback<Void> callback) {
        final CardPINUI cardPINUI = new CardPINUI(pinDisplayTextView);
        getD1Task().displayPhysicalCardPIN(cardId, cardPINUI, callback);
    }

    /**
     * Retrieves card activation method.
     *
     * @param cardId   Card ID.
     * @param callback Callback.
     */
    public void getActivationMethod(@NonNull final String cardId,
                                    @NonNull final D1Task.Callback<CardActivationMethod> callback) {
        getD1Task().getCardActivationMethod(cardId, callback);
    }

    /**
     * Retrieves the list of digital cards for D1Push.
     *
     * @param cardId   Card ID.
     * @param callback Callback.
     */
    public void getDigitalCardListD1Push(@NonNull final String cardId,
                                         @NonNull final D1Task.Callback<List<DigitalCard>> callback) {
        getD1Task().getDigitalCardList(cardId, callback);
    }

    /**
     * Retrieves the list of digital cards for D1Pay.
     *
     * @param callback Callback.
     */
    public void getDigitalCardListD1Pay(@NonNull final D1Task.Callback<Map<String, D1PayDigitalCard>> callback) {
        // TODO: workaround for synchronous API - should be fixed in the future
        new Thread(() -> {
            getD1Task().getD1PayWallet().getDigitalCardList(callback);
        }).start();
    }

    /**
     * Retrieves the D1Pay digital card for an associated card ID.
     *
     * @param cardId   Card ID.
     * @param callback Callback.
     */
    public void getDigitalCardD1Pay(@NonNull final String cardId,
                                    @NonNull final D1Task.Callback<D1PayDigitalCard> callback) {
        // TODO: workaround for synchronous API - should be fixed in the future
        new Thread(() -> {
            getD1Task().getD1PayWallet().getDigitalCard(cardId, callback);
        }).start();
    }

    /**
     * Resumes the D1Pay digital card.
     *
     * @param cardId      Card ID.
     * @param digitalCard Digital card.
     * @param callback    Callback.
     */
    public void resumeD1PayDigitalCard(@NonNull final String cardId,
                                       @NonNull final D1PayDigitalCard digitalCard,
                                       @NonNull final D1Task.Callback<Boolean> callback) {
        getD1Task().getD1PayWallet().updateDigitalCard(cardId, digitalCard, CardAction.RESUME, callback);
    }

    /**
     * Suspends the D1Pay digital card.
     *
     * @param cardId      Card ID.
     * @param digitalCard Digital card.
     * @param callback    Callback.
     */
    public void suspendD1PayDigitalCard(@NonNull final String cardId,
                                        @NonNull final D1PayDigitalCard digitalCard,
                                        @NonNull final D1Task.Callback<Boolean> callback) {
        getD1Task().getD1PayWallet().updateDigitalCard(cardId, digitalCard, CardAction.SUSPEND, callback);
    }

    /**
     * Deletes the D1Pay digital card.
     *
     * @param cardId      Card ID.
     * @param digitalCard Digital card.
     * @param callback    Callback.
     */
    public void deleteD1PayDigitalCard(@NonNull final String cardId,
                                       @NonNull final D1PayDigitalCard digitalCard,
                                       @NonNull final D1Task.Callback<Boolean> callback) {
        getD1Task().getD1PayWallet().updateDigitalCard(cardId, digitalCard, CardAction.DELETE, callback);
    }

    /**
     * Sets the D1Pay digital card as the default payment card.
     *
     * @param cardId   Card ID.
     * @param callback Callback.
     */
    public void setDefaultD1PayDigitalCard(@NonNull final String cardId, @NonNull final D1Task.Callback<Void> callback) {
        getD1Task().getD1PayWallet().setDefaultPaymentDigitalCard(cardId, callback);
    }

    /**
     * Unsets the D1Pay digital card as the default payment card.
     *
     * @param callback Callback.
     */
    public void unsetDefaultD1PayDigitalCard(@NonNull final D1Task.Callback<Void> callback) {
        getD1Task().getD1PayWallet().unsetDefaultPaymentDigitalCard(callback);
    }

    /**
     * Suspends the digital card.
     *
     * @param cardId      Card ID.
     * @param digitalCard Digital card.
     * @param callback    Callback.
     */
    public void suspendDigitalCard(@NonNull final String cardId,
                                   @NonNull final DigitalCard digitalCard,
                                   @NonNull final D1Task.Callback<Boolean> callback) {
        getD1Task().updateDigitalCard(cardId, digitalCard, CardAction.SUSPEND, callback);
    }

    /**
     * Resumes the digital card.
     *
     * @param cardId      Card ID.
     * @param digitalCard Digital card.
     * @param callback    Callback.
     */
    public void resumeDigitalCard(@NonNull final String cardId,
                                  @NonNull final DigitalCard digitalCard,
                                  @NonNull final D1Task.Callback<Boolean> callback) {
        getD1Task().updateDigitalCard(cardId, digitalCard, CardAction.RESUME, callback);
    }

    /**
     * Deletes the digital card.
     *
     * @param cardId      Card ID.
     * @param digitalCard Digital card.
     * @param callback    Callback.
     */
    public void deleteDigitalCard(@NonNull final String cardId,
                                  @NonNull final DigitalCard digitalCard,
                                  @NonNull final D1Task.Callback<Boolean> callback) {
        getD1Task().updateDigitalCard(cardId, digitalCard, CardAction.DELETE, callback);
    }

    /**
     * Updates the push token.
     *
     * @param pushToken Push token to update.
     * @param callback  Callback.
     */
    public void setPushToken(@NonNull final String pushToken, @NonNull final D1Task.Callback<Void> callback) {
        synchronized (INSTANCE) {
            if (mSdkConfigurationState.getValue() == SdkConfigState.CONFIGURED) {
                // SDK already configured.
                getD1Task().updatePushToken(pushToken, callback);
            } else {
                // SDK not yet configured.
                // Notify callback to not block the flow.
                callback.onSuccess(null);
            }
        }
    }

    /**
     * Gets D1Pay card digitization state.
     *
     * @param cardId   Card ID.
     * @param callback Callback.
     */
    public void getD1PayDigitizationState(@NonNull final String cardId,
                                          @NonNull final D1Task.Callback<CardDigitizationState> callback) {
        getD1Task().getD1PayWallet().getCardDigitizationState(cardId, callback);
    }

    /**
     * Handles the push message.
     *
     * @param remoteMessage Push message.
     * @param callback      Callback.
     */
    public void processPushMessage(@NonNull final RemoteMessage remoteMessage,
                                   @NonNull final D1Task.Callback<Map<PushResponseKey, String>> callback) {
        synchronized (INSTANCE) {
            if (mSdkConfigurationState.getValue() == SdkConfigState.CONFIGURED) {
                // SDK already configured
                getD1Task().processNotification(remoteMessage.getData(), callback);
            } else {
                // notify to not block the flow
                callback.onSuccess(null);
            }
        }
    }

    /**
     * Registers the CardDataChangedListener - D1Push.
     *
     * @param cardDataChangedListener CardDataChangedListener.
     */
    public void registerCardDataChangeListener(@NonNull final CardDataChangedListener cardDataChangedListener) {
        getD1Task().registerCardDataChangedListener(cardDataChangedListener);
    }

    /**
     * Unregisters the CardDataChangedListener - D1Push.
     */
    public void unregisterCardDataChangeListener() {
        getD1Task().unRegisterCardDataChangedListener();
    }

    /**
     * Registers the CardDataChangedListener - D1Pay.
     *
     * @param cardDataChangedListener CardDataChangedListener.
     */
    public void registerCardDataChangeListenerD1Pay(@NonNull final D1PayDataChangedListener cardDataChangedListener) {
        getD1Task().getD1PayWallet().registerD1PayDataChangedListener(cardDataChangedListener);
    }

    /**
     * Unregisters the CardDataChangedListener - D1Pay.
     */
    public void unregisterCardDataChangeListenerD1Pay() {
        getD1Task().getD1PayWallet().unRegisterD1PayDataChangedListener();
    }

    /**
     * Digitizes card for D1Pay.
     *
     * @param cardId   Card ID.
     * @param callback Callback.
     */
    public void digitizeD1PayCard(@NonNull final String cardId, @NonNull final D1Task.Callback<Void> callback) {
        getD1Task().getD1PayWallet().addDigitalCard(cardId, callback);
    }

    /**
     * Replenishes the card.
     *
     * @param cardId                       Card ID.
     * @param deviceAuthenticationCallback Device authentication callback.
     * @param callback                     Callback.
     */
    public void replenish(@NonNull final String cardId,
                          @NonNull final DeviceAuthenticationCallback deviceAuthenticationCallback,
                          @NonNull final D1Task.Callback<Void> callback) {
        getD1Task().getD1PayWallet().replenish(cardId, true, deviceAuthenticationCallback, callback);
    }

    /**
     * Runs the payment in manual mode.
     *
     * @param cardId   Card ID.
     */
    public void manualMode(@NonNull final String cardId) {
        D1PayConfigParams.getInstance().setManualModeContactlessTransactionListener(mD1PayTransactionListener);
        getD1Task().getD1PayWallet().startManualModePayment(cardId);
    }

    /**
     * Retrieves the D1 SDK versions.
     *
     * @return D1 SDK versions.
     */
    public String getLibVersions() {
        return D1Task.getSDKVersions().toString();
    }

    /**
     * Retrieves the transaction history.
     *
     * @param cardId   Card ID.
     * @param callback Callback.
     */
    public void getD1PayTransactionHistory(@NonNull final String cardId,
                                           @NonNull final D1Task.Callback<TransactionHistory> callback) {
        getD1Task().getD1PayWallet().getTransactionHistory(cardId, callback);
    }

    /**
     * Sets the current push token to D1 SDK.
     */
    private void getCurrentPushToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                return;
            }

            // Get new FCM registration token
            final String token = task.getResult();
            getD1Task().updatePushToken(token, new D1Task.Callback<Void>() {
                @Override
                public void onSuccess(final Void data) {
                    // nothing to do
                }

                @Override
                public void onError(@NonNull final D1Exception exception) {
                    // ignore error
                }
            });
        });
    }

    /**
     * Retrieves the {@code D1PayTransactionListener} instance.
     *
     * @return {@code D1PayTransactionListener} instance.
     */
    @Nullable
    public D1PayTransactionListener getD1PayTransactionListener() {
        return mD1PayTransactionListener;
    }

    /**
     * Retrieves the {@code D1Task} instance.
     *
     * @return {@code D1Task} instance.
     */
    @NonNull
    private D1Task getD1Task() {
        if (mD1Task == null) {
            throw new IllegalStateException("Need to configure D1 SDK first.");
        }

        return mD1Task;
    }

    enum SdkConfigState {
        NOT_CONFIGURED, WORKING, CONFIGURED, ERROR
    }
}
