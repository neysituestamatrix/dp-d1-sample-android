/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.thalesgroup.gemalto.d1.validation.R;
import com.thalesgroup.mobile.d1sample.sdk.D1Helper;
import com.thalesgroup.mobile.d1sample.ui.splash.SplashFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Main activity", "Launching");
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


}
