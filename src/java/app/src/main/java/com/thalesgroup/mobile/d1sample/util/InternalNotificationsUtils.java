/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.thalesgroup.mobile.d1sample.sdk.payment.PaymentData;
import com.thalesgroup.mobile.d1sample.sdk.payment.PaymentState;

/**
 * Notification utility.
 */
public final class InternalNotificationsUtils {

    //region Defines

    private static final String ACTION_PAYMENT_COUNTDOWN = "com.thalesgroup.d1sample.paymentcountdown";
    private static final String ACTION_VALUE_REMAINING = "ValueRemaining";

    //endregion


    //region Public API - Payment

    public interface PaymentStateChangeHandler {
        void onStateChanged(@NonNull final PaymentState state, @Nullable final PaymentData data);

    }

    public interface PaymentCountdownChangeHandler {
        void onCountdownChanged(final int remainingSec);
    }

    public static BroadcastReceiver registerForPaymentCountdown(@NonNull final Context context,
                                                                @NonNull final PaymentCountdownChangeHandler handler) {
        final BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                final int remainingSec = intent.getIntExtra(ACTION_VALUE_REMAINING, 0);

                handler.onCountdownChanged(remainingSec);
            }
        };

        // Handle enrollment state changes.
        final IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_PAYMENT_COUNTDOWN);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);

        return receiver;
    }

    public static void updatePaymentCountdown(@NonNull final Context context, final int remainingSec) {
        final Intent paymentState = new Intent(ACTION_PAYMENT_COUNTDOWN);
        paymentState.putExtra(ACTION_VALUE_REMAINING, remainingSec);

        LocalBroadcastManager.getInstance(context).sendBroadcast(paymentState);
    }
    //endregion

}
