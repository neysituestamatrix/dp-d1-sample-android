/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.thalesgroup.gemalto.d1.D1Exception;
import com.thalesgroup.gemalto.d1.d1pay.AuthenticationParameter;
import com.thalesgroup.gemalto.d1.d1pay.ContactlessTransactionListener;
import com.thalesgroup.gemalto.d1.d1pay.DeviceAuthenticationCallback;
import com.thalesgroup.gemalto.d1.d1pay.DeviceAuthenticationTimeoutCallback;
import com.thalesgroup.gemalto.d1.d1pay.TransactionData;
import com.thalesgroup.gemalto.d1.d1pay.VerificationMethod;
import com.thalesgroup.gemalto.d1.validation.R;
import com.thalesgroup.mobile.d1sample.fcm.D1PayFirebaseService;
import com.thalesgroup.mobile.d1sample.sdk.D1Helper;
import com.thalesgroup.mobile.d1sample.ui.splash.SplashFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MainActivity extends AppCompatActivity {
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String message = intent.getStringExtra(D1PayFirebaseService.NOTIFY_UI_MESSAGE);
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        }
    };

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if (checkMandatoryPermissions()) {
            showFragment(SplashFragment.newInstance(), false);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        D1Helper.getInstance().handleResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (final int result : grantResults) {
            if (result != 0) {
                Toast.makeText(this, "Cannot grant permission.", Toast.LENGTH_LONG).show();
                return;
            }
        }

        showFragment(SplashFragment.newInstance(), false);
    }

    @Override
    protected void onStart() {
        super.onStart();

        LocalBroadcastManager.getInstance(this)
                             .registerReceiver(mBroadcastReceiver, new IntentFilter(D1PayFirebaseService.NOTIFY_UI_REQUEST));
    }

    @Override
    protected void onStop() {
        super.onStop();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    /**
     * Shows a new Fragment.
     *
     * @param fragment       Fragment to show.
     * @param addToBackstack {@code True} if Fragment should be added to the backstack, else {@code false}.
     */
    public void showFragment(final Fragment fragment, final boolean addToBackstack) {
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                                                                                   .replace(R.id.container, fragment);
        if (addToBackstack) {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
    }

    /**
     * Pops a Fragment from the backstack.
     */
    public void popFromBackstack() {
        getSupportFragmentManager().popBackStack();
    }

    /**
     * Gets the ContactlessTransactionListener.
     *
     * @return ContactlessTransactionListener.
     */
    public ContactlessTransactionListener getContactlessTransactionListener() {
        return mContactlessTransactionListener;
    }

    /**
     * Shows a progress dialog.
     *
     * @param message Message to display.
     */
    public void showProgressDialog(@NonNull final String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(message);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    /**
     * Hides the progress dialog.
     */
    public void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    /**
     * Checks the required runtime permissions.
     *
     * @return {@code True} if all permissions are present, else {@code false}.
     */
    private boolean checkMandatoryPermissions() {
        try {
            // Get list of all permissions defined in app manifest.
            final PackageInfo info = getPackageManager()
                    .getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            return checkPermissions(info.requestedPermissions);
        } catch (final PackageManager.NameNotFoundException exception) {
            // App package must be present.
            throw new IllegalStateException(exception);
        }
    }

    /**
     * Checks for runtime permission.
     *
     * @param permissions List of permissions.
     * @return {@code True} if permissions are present, else {@code false}.
     */
    private boolean checkPermissions(final String... permissions) {
        // Update list of permissions based on granted status.
        final List<String> permissionsToCheck = new ArrayList<>();
        for (final String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PermissionChecker.PERMISSION_GRANTED) {
                //noinspection StatementWithEmptyBody
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    // Here we can display some description why we need this permission.
                }

                permissionsToCheck.add(permission);
            }
        }

        // Some permissions are not granted. Ask user for them.
        if (!permissionsToCheck.isEmpty()) {
            final String[] notGrantedArray = permissionsToCheck.toArray(new String[0]);
            ActivityCompat.requestPermissions(this, notGrantedArray, 0);
        }

        return permissionsToCheck.isEmpty();
    }

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
            Toast.makeText(MainActivity.this, "Authentication failed", Toast.LENGTH_LONG).show();
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

    /**
     * Listener for transaction events - triggered during payment events.
     */
    private final ContactlessTransactionListener mContactlessTransactionListener = new ContactlessTransactionListener() {
        @Override
        public void onTransactionStarted() {
            // Display transaction is ongoing
            Toast.makeText(MainActivity.this, "Transaction started", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onAuthenticationRequired(@NonNull final VerificationMethod method) {
            // Only applicable for 2-TAP experience
            // Display transaction details and tell consumer to authenticate
            final TransactionData transactionData = this.getTransactionData();
            final double amount = transactionData != null ? transactionData.getAmount() : -1;
            final AuthenticationParameter authenticationParameter = new AuthenticationParameter(MainActivity.this,
                                                                                                "Authentication required",
                                                                                                "Subtitle",
                                                                                                String.format(Locale.ENGLISH,
                                                                                                              "Amount: %f",
                                                                                                              amount),
                                                                                                "Cancel",
                                                                                                mDeviceAuthenticationCallback);

            this.startAuthenticate(authenticationParameter);
            if (transactionData != null) {
                transactionData.wipe();
            }
        }

        @Override
        public void onReadyToTap() {
            // Only applicable for 2-TAP experience
            // Inform customer application is ready for 2nd TAP.
            // Display transaction details and display the remaining time for the 2nd TAP
            final TransactionData transactionData = this.getTransactionData();
            final double amount = transactionData != null ? transactionData.getAmount() : -1;
            Toast.makeText(MainActivity.this,
                           String.format(Locale.ENGLISH, "Perform 2nd tap for amount: %f", amount),
                           Toast.LENGTH_LONG).show();
            if (transactionData != null) {
                transactionData.wipe();
            }

            // Register the timeout callback to update the user on remaining time for the 2nd tap.
            this.registerDeviceAuthTimeoutCallback(new DeviceAuthenticationTimeoutCallback() {
                @Override
                public void onTimer(final int remain) {
                    // The mobile application should update the countdown screen with current "remaining" time.
                }

                @Override
                public void onTimeout() {
                    // The mobile application should inform end user of the timeout error.
                }
            });
        }

        @Override
        public void onTransactionCompleted() {
            // The transaction has been completed successfully on the mobile app.
            // Display transaction status success and details
            final TransactionData transactionData = this.getTransactionData();
            final double amount = transactionData != null ? transactionData.getAmount() : -1;
            Toast.makeText(MainActivity.this,
                           String.format(Locale.ENGLISH, "Payment completed: %f", amount),
                           Toast.LENGTH_LONG).show();
            if (transactionData != null) {
                transactionData.wipe();
            }
        }

        @Override
        public void onError(@NonNull final D1Exception error) {
            // The transaction failed due to an error.
            // Mobile application should get detailed information from the "error" param and inform the end user.
            Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    };
}
