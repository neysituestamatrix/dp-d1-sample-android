/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.payment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thalesgroup.gemalto.d1.validation.R;
import com.thalesgroup.mobile.d1sample.sdk.payment.PaymentData;

import java.util.Locale;

/**
 * Payment ready fragment.
 */
public class FragmentPaymentReady extends AbstractPaymentFragment {

    //region Defines

    //    private TextView mMessageTextView;
    private TextView mSecondsTextView;

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
        final View root = inflater.inflate(R.layout.fragment_payment_ready, container, false);

        mSecondsTextView = root.findViewById(R.id.remaining_seconds);

        final TextView amountTextView = root.findViewById(R.id.amount);
        final PaymentData data = getPaymentActivity().getSecondTapData();

        if (data != null) {
            if (data.getAmount() > 0) {
                amountTextView.setText(String.format(Locale.getDefault(), "%s %s", data.getAmount(), data.getCurrency()));
            } else {
                amountTextView.setVisibility(View.GONE);
            }
        }
        return root;
    }

    @Override
    public void onPaymentCountdownChanged(final int remainingSeconds) {
        mSecondsTextView.setText(String.format(Locale.getDefault(), "%d s", remainingSeconds));
    }

    //endregion

}
