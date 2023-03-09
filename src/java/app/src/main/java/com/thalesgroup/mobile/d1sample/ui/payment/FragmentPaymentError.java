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
import com.thalesgroup.mobile.d1sample.sdk.payment.PaymentErrorData;

/**
 * Payment error fragment.
 */
public class FragmentPaymentError extends AbstractPaymentFragment {

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
        final View root = inflater.inflate(R.layout.fragment_payment_error, container, false);
        final TextView messageTextView = root.findViewById(R.id.message);

        final PaymentErrorData data = getPaymentActivity().getErrorData();
        if (data != null) {
            messageTextView.setText(data.getMessage());
        }
        return root;
    }

    //endregion

}
