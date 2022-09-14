/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.fcm;

import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.thalesgroup.gemalto.d1.D1Exception;
import com.thalesgroup.gemalto.d1.D1Task;
import com.thalesgroup.gemalto.d1.d1pay.DeviceAuthenticationCallback;
import com.thalesgroup.mobile.d1sample.sdk.D1Helper;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class D1PayFirebaseService extends FirebaseMessagingService {
    public static final String NOTIFY_UI_REQUEST = "NOTIFY_UI_REQUEST";
    public static final String NOTIFY_UI_MESSAGE = "NOTIFY_UI_MESSAGE";

    @Override
    public void onMessageReceived(@NonNull final RemoteMessage message) {
        notifyUI("Push message received.");

        D1Helper.getInstance().processPushMessage(message, new D1Task.Callback<String>() {
            @Override
            public void onSuccess(final String data) {
                notifyUI("Process push message success");
                if (data != null && data.length() > 0) {
                    notifyUI(data);
                    D1Helper.getInstance().replenish(data, mDeviceAuthenticationCallback, new D1Task.Callback<Void>() {
                        @Override
                        public void onSuccess(final Void data) {
                            notifyUI("Replenish success");
                        }

                        @Override
                        public void onError(@NonNull final D1Exception exception) {
                            notifyUI(exception.getLocalizedMessage() != null ? exception.getLocalizedMessage()
                                                                             : "Undef error");
                        }
                    });
                }
            }

            @Override
            public void onError(@NonNull final D1Exception exception) {
                notifyUI(exception.getLocalizedMessage() != null ? exception.getLocalizedMessage() : "Undef error");
            }
        });
    }

    @Override
    public void onNewToken(@NonNull final String token) {
        notifyUI(token);
        D1Helper.getInstance().setPushToken(token, new D1Task.Callback<Void>() {
            @Override
            public void onSuccess(final Void data) {
                notifyUI("Updated push token success");
            }

            @Override
            public void onError(@NonNull final D1Exception exception) {
                notifyUI(exception.getLocalizedMessage() != null ? exception.getLocalizedMessage() : "Undef error");
            }
        });
    }

    /**
     * Notifies UI.
     *
     * @param message Message.
     */
    private void notifyUI(@NonNull final String message) {
        final LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getBaseContext());

        final Intent intent = new Intent();
        intent.setAction(NOTIFY_UI_REQUEST);
        intent.putExtra(NOTIFY_UI_MESSAGE, message);
        localBroadcastManager.sendBroadcast(intent);
    }

    private final DeviceAuthenticationCallback mDeviceAuthenticationCallback = new DeviceAuthenticationCallback() {
        @Override
        public void onSuccess() {
            notifyUI("Authentication OK.");
        }

        @Override
        public void onFailed() {
            // User authentication failed, the mobile app may ask end user to retry
            notifyUI("Authentication Failed.");
        }

        @Override
        public void onError(final int fpErrorCode) {
            // For BIOMETRIC only
            // Error happened while doing BIOMETRIC authenticate (e.g using wrong finger too many times and the sensor is
            // locked)
            // Depending on the fpErrorCode, the mobile application should troubleshoot the end user.
            notifyUI(String.format(Locale.ENGLISH, "Authentication Error: %d.", fpErrorCode));
        }

        @Override
        public void onHelp(final int fpCode, @NonNull final CharSequence detail) {
            // For BIOMETRIC only
            // Mobile application may show the fpDetail message to the end user
            notifyUI(String.format(Locale.ENGLISH, "Authentication Help: %s, code: %d.", detail, fpCode));
        }
    };
}
