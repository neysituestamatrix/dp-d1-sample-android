/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.splash;

import android.app.Notification;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.thalesgroup.gemalto.d1.validation.R;
import com.thalesgroup.mobile.d1sample.MainActivity;
import com.thalesgroup.mobile.d1sample.ui.base.AbstractBaseFragment;
import com.thalesgroup.mobile.d1sample.ui.login.LoginFragment;
import com.thalesgroup.mobile.d1sample.util.NotificationUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

/**
 * Login Fragment.
 */
public class SplashFragment extends AbstractBaseFragment<SplashViewModel> {

    /**
     * Creates a new instance of {@code SplashFragment}.
     *
     * @return Instance of {@code SplashFragment}.
     */
    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        mViewModel.getIsOperationSuccesfull().observe(getViewLifecycleOwner(), isOperationSuccessful -> {
            hideProgressDialog();
            if (isOperationSuccessful) {
                showFragment(LoginFragment.newInstance(), false);
            }
        });

        mViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            hideProgressDialog();
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        });

        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        showProgressDialog("Initializing.");
        final Notification notification = NotificationUtil.getNotification(getActivity().getApplicationContext(),
                                                                           "This notification is posted to run internal "
                                                                           + "operation of mg sdk",
                                                                           "SDK_NOTIFICATION");

        mViewModel.configure(getActivity().getApplicationContext(),
                             getActivity(),
                             ((MainActivity) getActivity()).getContactlessTransactionListener(),
                             notification);
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    protected SplashViewModel createViewModel() {
        return new ViewModelProvider(this).get(SplashViewModel.class);
    }
}