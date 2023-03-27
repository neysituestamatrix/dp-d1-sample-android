/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.payment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thalesgroup.gemalto.d1.validation.R;
import com.thalesgroup.mobile.d1sample.sdk.payment.PaymentData;

import java.util.Locale;
import java.util.Objects;

/**
 * Payment success fragment.
 */
public class FragmentPaymentSuccess extends AbstractPaymentFragment {

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
        final View root = inflater.inflate(R.layout.fragment_payment_success, container, false);
        final TextView amountTextView = root.findViewById(R.id.amount);

        final PaymentData data = getPaymentActivity().getSuccessData();
        if (data != null) {
            amountTextView.setText(String.format(Locale.getDefault(), "%s %s", data.getAmount(), data.getCurrency()));
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> requireActivity().finish(), 3000);

        return root;
    }

    //endregion

}
