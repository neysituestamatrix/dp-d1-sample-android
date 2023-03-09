/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.sdk.payment;

/**
 * Payment state.
 */
public enum PaymentState {
    STATE_NONE,
    STATE_ON_TRANSACTION_STARTED,
    STATE_ON_AUTHENTICATION_REQUIRED,
    STATE_ON_READY_TO_TAP,
    STATE_ON_TRANSACTION_COMPLETED,
    STATE_ON_ERROR
}
