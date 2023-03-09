/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.payment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thalesgroup.gemalto.d1.validation.R;

/**
 * Payment started fragment.
 */
public class FragmentPaymentStarted extends AbstractPaymentFragment {

    //region Life Cycle

    @Override
    public int getFragmentCaption() {
        return R.string.label_payment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        // TODO: Load fragment_payment_started_card_visual
        return inflater.inflate(R.layout.fragment_payment_started, container, false);
    }

    //endregion

}
