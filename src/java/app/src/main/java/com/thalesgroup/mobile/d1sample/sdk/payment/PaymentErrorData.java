/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.sdk.payment;

/**
 * Payment error data.
 */
public class PaymentErrorData extends PaymentData {
    final String mCode;
    final String mMessage;

    /**
     * Creates a new instance of {@code PaymentErrorData}.
     *
     * @param code     Error code.
     * @param message  Error message.
     * @param amount   Amount.
     * @param currency Currency.
     */
    public PaymentErrorData(final String code, final String message, final double amount, final String currency) {
        super(amount, currency);

        mCode = code;
        mMessage = message;
    }

    /**
     * Retrieves the error code.
     *
     * @return Error code.
     */
    public String getCode() {
        return mCode;
    }

    /**
     * Retrieves the error message.
     *
     * @return Error message.
     */
    public String getMessage() {
        return mMessage;
    }
}
