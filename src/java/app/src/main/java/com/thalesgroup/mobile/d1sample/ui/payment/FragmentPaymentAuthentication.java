/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.payment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.thalesgroup.gemalto.d1.d1pay.AuthenticationParameter;
import com.thalesgroup.gemalto.d1.d1pay.DeviceAuthenticationCallback;
import com.thalesgroup.gemalto.d1.validation.R;
import com.thalesgroup.mobile.d1sample.sdk.D1Helper;
import com.thalesgroup.mobile.d1sample.sdk.payment.D1PayTransactionListener;
import com.thalesgroup.mobile.d1sample.sdk.payment.PaymentData;

import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;

/**
 * Payment authentication fragment.
 */
public class FragmentPaymentAuthentication extends AbstractPaymentFragment {

    private PaymentData mAuthData;

    //endregion

    //region Life Cycle

    @Override
    public int getFragmentCaption() {
        return R.string.label_payment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_payment_authentication, container, false);


        /**
         * There is a button that allows to re-try the authentication if the prompt was dismissed
         */
        final View btnAuthenticate = root.findViewById(R.id.btn_authenticate);
        btnAuthenticate.setOnClickListener(view -> doAuthenticate());

        mAuthData = getPaymentActivity().getAuthData();

        doAuthenticate();
        return root;
    }

    /**
     * Authenticates he user.
     */
    private void doAuthenticate() {
        if (mAuthData != null) {

            final AuthenticationParameter authenticationParameter = new AuthenticationParameter(requireActivity(),
                                                                                                "Authentication required",
                                                                                                "Subtitle",
                                                                                                String.format(Locale.ENGLISH,
                            "Amount: %f %s",
                            mAuthData.getAmount(), mAuthData.getCurrency()),
                                                                                                "Cancel",
                                                                                                mDeviceAuthenticationCallback);

            Objects.requireNonNull(D1Helper.getInstance().getD1PayTransactionListener())
                   .startAuthenticate(authenticationParameter);
        }
    }

    //endregion

    /**
     * Listener for device authentication events - triggered when user needs to authenticate.
     */
    private final DeviceAuthenticationCallback mDeviceAuthenticationCallback = new DeviceAuthenticationCallback() {
        @Override
        public void onSuccess() {
            // User authentication was successful
            // payment process will continue to the next stage: onReadyToTap()
        }

        @Override
        public void onFailed() {
            // User authentication failed, the mobile app may ask end user to retry
            Toast.makeText(getContext(), "Authentication failed", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(final int fpErrorCode) {
            // For BIOMETRIC only
            // Error happened while doing BIOMETRIC authentication (for example, using wrong finger too many times and the
            // sensor is locked)
            // Base on the fpErrorCode, the mobile application should troubleshoot the end user.
        }

        @Override
        public void onHelp(final int fpCode, @NonNull final CharSequence detail) {
            // For BIOMETRIC only
            // Mobile application may show the fpDetail message to the end user
        }
    };
}
