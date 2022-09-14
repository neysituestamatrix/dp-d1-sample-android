package com.thalesgroup.mobile.d1sample.ui.home;

/*
 * Copyright © 2022 THALES. All rights reserved.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.thalesgroup.gemalto.d1.validation.BuildConfig;
import com.thalesgroup.gemalto.d1.validation.R;
import com.thalesgroup.mobile.d1sample.ui.base.AbstractBaseFragment;
import com.thalesgroup.mobile.d1sample.ui.d1paydigitalcardlist.D1PayDigitalCardListFragment;
import com.thalesgroup.mobile.d1sample.ui.login.LoginFragment;
import com.thalesgroup.mobile.d1sample.ui.physicalcardlist.PhysicalCardListFragment;
import com.thalesgroup.mobile.d1sample.ui.virtualcardlist.VirtualCardListFragment;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

/**
 * Login Fragment.
 */
public class HomeFragment extends AbstractBaseFragment<HomeViewModel> {

    /**
     * Creates a new instance of {@code LoginFragment}.
     *
     * @return Instance of {@code LoginFragment}.
     */
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.home_fragment, container, false);

        mViewModel.getIsOperationSuccesfull().observe(getViewLifecycleOwner(), isLogoutSuccessful -> {
            hideProgressDialog();
            if (isLogoutSuccessful) {
                popFromBackstack();
                showFragment(LoginFragment.newInstance(), false);
            }
        });

        mViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            hideProgressDialog();
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        });

        view.findViewById(R.id.bt_logout).setOnClickListener(v -> {
            showProgressDialog("Logout in progress.");
            mViewModel.logout();
        });

        view.findViewById(R.id.bt_physical_card_list).setOnClickListener(v -> {
            showFragment(PhysicalCardListFragment.newInstance(), true);
        });

        view.findViewById(R.id.bt_logout).setOnClickListener(v -> {
            mViewModel.logout();
        });

        view.findViewById(R.id.bt_virtual_card_list).setOnClickListener(v -> {
            showFragment(VirtualCardListFragment.newInstance(), true);
        });

        view.findViewById(R.id.bt_d1pay_digital_card_list).setOnClickListener(v -> {
            showFragment(D1PayDigitalCardListFragment.newInstance(), true);
        });

        ((TextView) view.findViewById(R.id.tv_version))
                .setText(String.format(Locale.ENGLISH, "%s-%d", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));

        ((TextView) view.findViewById(R.id.tv_version_sdk)).setText(mViewModel.getLibVersions());

        return view;
    }

    @NonNull
    @Override
    protected HomeViewModel createViewModel() {
        return new ViewModelProvider(this).get(HomeViewModel.class);
    }
}