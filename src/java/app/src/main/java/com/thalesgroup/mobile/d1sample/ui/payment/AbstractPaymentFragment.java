/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.payment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.thalesgroup.mobile.d1sample.sdk.payment.PaymentState;

/**
 * Abstract payment fragment.
 */
public abstract class AbstractPaymentFragment extends Fragment {

    /**
     * Retrieves the fragment caption.
     *
     * @return Fragment caption.
     */
    @StringRes
    public abstract int getFragmentCaption();

    /**
     * Payment status changed - optional to be implemented.
     *
     * @param state Payment state.
     */
    public void onPaymentStatusChanged(@NonNull final PaymentState state) {
        // Optional for other fragments.
    }

    /**
     * Payment countdown changed - optional to be implemented.
     *
     * @param remainingSeconds Remaining seconds for payment.
     */
    public void onPaymentCountdownChanged(@NonNull final int remainingSeconds) {
        // Optional for other fragments.
    }

    /**
     * Retrieves the payment activity.
     *
     * @return {@code PaymentActivity} instance.
     */
    public PaymentActivity getPaymentActivity() {
        return (PaymentActivity) getActivity();
    }
}
