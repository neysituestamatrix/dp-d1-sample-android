/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.splash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.thalesgroup.gemalto.d1.validation.R;
import com.thalesgroup.mobile.d1sample.ui.base.AbstractBaseFragment;
import com.thalesgroup.mobile.d1sample.ui.login.LoginFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

/**
 * Login Fragment.
 */
public class SplashFragment extends AbstractBaseFragment<SplashViewModel> {
    Button mTryAgainButton;

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
        final View view = inflater.inflate(R.layout.fragment_splash, container, false);
        mTryAgainButton = view.findViewById(R.id.bt_try_again);

        mViewModel.getIsOperationSuccesfull().observe(getViewLifecycleOwner(), isOperationSuccessful -> {
            hideProgressDialog();
            if (isOperationSuccessful) {
                showFragment(LoginFragment.newInstance(), false);
            }
        });

        mViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            hideProgressDialog();
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
            mTryAgainButton.setVisibility(View.VISIBLE);
        });

        mTryAgainButton.setOnClickListener(v -> {
            mTryAgainButton.setVisibility(View.GONE);

            showProgressDialog("Initializing.");

            mViewModel.configure(getActivity());
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mTryAgainButton.setVisibility(View.GONE);

        showProgressDialog("Initializing.");

        mViewModel.configure(getActivity());
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