/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.base;

import android.os.Bundle;

import com.thalesgroup.mobile.d1sample.MainActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Abstract fragment.
 */
public abstract class AbstractBaseFragment<VM extends BaseViewModel> extends Fragment {
    protected VM mViewModel;
    protected static final String ARG_CARD_ID = "ARG_CARD_ID";

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = createViewModel();
    }

    @Override
    public void onStop() {
        super.onStop();

        hideProgressDialog();
    }

    /**
     * Creates the {@code ViewModel} instance.
     *
     * @return {@code ViewModel} instance.
     */
    @NonNull
    protected abstract VM createViewModel();

    /**
     * Adds a new {@code Fragment}.
     *
     * @param fragment       {@code Fragment} to add.
     * @param addToBackstack {@code True} if {@code Fragment} should be added to backstack, else {@code false}.
     */
    protected void showFragment(final Fragment fragment, final boolean addToBackstack) {
        ((MainActivity) getActivity()).showFragment(fragment, addToBackstack);
    }

    /**
     * Pops a Fragment from the backstack.
     */
    protected void popFromBackstack() {
        ((MainActivity) getActivity()).popFromBackstack();
    }

    /**
     * Shows a progress dialog.
     *
     * @param message Message to display.
     */
    protected void showProgressDialog(@NonNull final String message) {
        final MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.showProgressDialog(message);
        }
    }

    /**
     * Hides the progress dialog.
     */
    protected void hideProgressDialog() {
        final MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.hideProgressDialog();
        }
    }
}