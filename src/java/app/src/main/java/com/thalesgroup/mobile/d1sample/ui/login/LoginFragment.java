/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.thalesgroup.gemalto.d1.validation.R;
import com.thalesgroup.mobile.d1sample.ui.base.AbstractBaseFragment;
import com.thalesgroup.mobile.d1sample.ui.home.HomeFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

/**
 * Login Fragment.
 */
public class LoginFragment extends AbstractBaseFragment<LoginViewModel> {

    /**
     * Creates a new instance of {@code LoginFragment}.
     *
     * @return Instance of {@code LoginFragment}.
     */
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        mViewModel.getIsOperationSuccesfull().observe(getViewLifecycleOwner(), isLoginSuccessful -> {
            hideProgressDialog();
            if (isLoginSuccessful) {
                showFragment(HomeFragment.newInstance(), false);
            }
        });

        mViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            hideProgressDialog();
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        });

        final View view = inflater.inflate(R.layout.login_fragment, container, false);

        view.findViewById(R.id.bt_login).setOnClickListener(v -> {
            showProgressDialog("Login in progress.");
            mViewModel.login();
        });

        return view;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    protected LoginViewModel createViewModel() {
        return new ViewModelProvider(this).get(LoginViewModel.class);
    }
}