package com.thalesgroup.mobile.d1sample.ui.payment;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.thalesgroup.gemalto.d1.validation.R;
import com.thalesgroup.mobile.d1sample.sdk.payment.PaymentData;
import com.thalesgroup.mobile.d1sample.sdk.payment.PaymentErrorData;
import com.thalesgroup.mobile.d1sample.sdk.payment.PaymentState;
import com.thalesgroup.mobile.d1sample.util.InternalNotificationsUtils;

/**
 * Payment activity.
 */
public class PaymentActivity extends AppCompatActivity {

    //region Defines

    private static final String FRAGMENT_TAG = AbstractPaymentFragment.class.getSimpleName();

    public static final String STATE_EXTRA_KEY = "STATE_EXTRA_KEY";
    public static final String PAYMENT_DATA_EXTRA_KEY = "PAYMENT_DATA_EXTRA_KEY";

    private BroadcastReceiver mPaymentCountdownReceiver;

    private PaymentErrorData mErrorData;
    private PaymentData mSuccessData;
    private PaymentData mAuthData;
    private PaymentData mSecondTapData;

    //endregion

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payment);


        // Register for payment activity.
        mPaymentCountdownReceiver = InternalNotificationsUtils.registerForPaymentCountdown(this, seconds -> {
            final AbstractPaymentFragment currentFragment = getCurrentFragment();
            if (currentFragment != null) {
                currentFragment.onPaymentCountdownChanged(seconds);
            }
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);

        if (intent.hasExtra(PAYMENT_DATA_EXTRA_KEY) && intent.hasExtra(STATE_EXTRA_KEY)) {

            final PaymentState state = (PaymentState) intent.getSerializableExtra(STATE_EXTRA_KEY);
            final PaymentData paymentData = (PaymentData) intent.getSerializableExtra(PAYMENT_DATA_EXTRA_KEY);

            final AbstractPaymentFragment currentFragment = getCurrentFragment();
            if (currentFragment != null) {
                currentFragment.onPaymentStatusChanged(state);
            }

            switch (state) {
                case STATE_ON_TRANSACTION_STARTED:
                    showFragment(new FragmentPaymentStarted(), false);
                    break;
                case STATE_ON_AUTHENTICATION_REQUIRED:
                    mAuthData = paymentData;
                    showFragment(new FragmentPaymentAuthentication(), false);
                    break;
                case STATE_ON_READY_TO_TAP:
                    mSecondTapData = paymentData;
                    showFragment(new FragmentPaymentReady(), false);
                    break;
                case STATE_ON_TRANSACTION_COMPLETED:
                    mSuccessData = paymentData;
                    showFragment(new FragmentPaymentSuccess(), false);
                    break;
                case STATE_ON_ERROR:
                    mErrorData = (PaymentErrorData) paymentData;
                    showFragment(new FragmentPaymentError(), false);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mPaymentCountdownReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mPaymentCountdownReceiver);
            mPaymentCountdownReceiver = null;
        }

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

    /**
     * Shows fragment.
     *
     * @param fragment       Fragment to show.
     * @param addToBackStack {@code True} if Fragment should be added to backstack, else {@code false}.
     */
    public void showFragment(final AbstractPaymentFragment fragment, final boolean addToBackStack) {
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, FRAGMENT_TAG);

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
    }

    /**
     * Retrieves the current fragment.
     *
     * @return Current fragment.
     */
    protected AbstractPaymentFragment getCurrentFragment() {
        return (AbstractPaymentFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
    }

    /**
     * Retrieves the authentication data.
     * @return Authentication data.
     */
    public PaymentData getAuthData() {
        return mAuthData;
    }

    /**
     * Retrieves the error data.
     *
     * @return Error data.
     */
    public PaymentErrorData getErrorData() {
        return mErrorData;
    }

    /**
     * Retrieves the success data.
     *
     * @return
     */
    public PaymentData getSuccessData() {
        return mSuccessData;
    }

    /**
     * Retrieves the second tap data.
     *
     * @return Second tap data.
     */
    public PaymentData getSecondTapData() {
        return mSecondTapData;
    }
}
