/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.sdk.payment;

import java.io.Serializable;

/**
 * Payment data.
 */
public class PaymentData implements Serializable {

    private static final long serialVersionUID = -8846799612852355120L;
    private final double mAmount;
    private final String mCurrency;

    /**
     * Creates a new instance of {@code PaymentData}.
     *
     * @param amount   Amount.
     * @param currency Currency.
     */
    public PaymentData(final double amount, final String currency) {
        mAmount = amount;
        mCurrency = currency;
    }

    /**
     * Retrieves the amount.
     *
     * @return Amount.
     */
    public double getAmount() {
        return mAmount;
    }

    /**
     * Retrieves the currency.
     *
     * @return Currency.
     */
    public String getCurrency() {
        return mCurrency;
    }


}


