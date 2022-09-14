/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.d1paydigitalcard;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.thalesgroup.gemalto.d1.validation.R;
import com.thalesgroup.gemalto.d1.validation.databinding.FragmentD1payDigitalCardDetailBinding;
import com.thalesgroup.mobile.d1sample.ui.base.AbstractBaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

/**
 * D1PayDigitalCardFragment Fragment.
 */
public class D1PayDigitalCardFragment extends AbstractBaseFragment<D1PayDigitalCardViewModel> {
    private String mCardId;
    private ImageView mIcon;
    private ConstraintLayout mCardBackground;

    /**
     * Creates a new instance of {@code D1PayDigitalCardFragment}.
     *
     * @return Instance of {@code D1PayDigitalCardFragment}.
     */
    public static D1PayDigitalCardFragment newInstance(@NonNull final String cardId) {
        final D1PayDigitalCardFragment fragment = new D1PayDigitalCardFragment();
        final Bundle args = new Bundle();
        args.putString(ARG_CARD_ID, cardId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mCardId = getArguments().getString(ARG_CARD_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        final FragmentD1payDigitalCardDetailBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_d1pay_digital_card_detail, container, false);
        binding.setLifecycleOwner(this);
        binding.setMViewModel(mViewModel);

        final View view = binding.getRoot();

        mCardBackground = view.findViewById(R.id.cl_card_layout);
        mIcon = view.findViewById(R.id.iv_icon);

        view.findViewById(R.id.bt_resume_d1pay_card_details).setOnClickListener(v -> {
            showProgressDialog("Operation in progress.");
            mViewModel.resumeD1PayDigitalCard(mCardId);
        });

        view.findViewById(R.id.bt_suspend_d1pay_card_details).setOnClickListener(v -> {
            showProgressDialog("Operation in progress.");
            mViewModel.suspendD1PayDigitalCard(mCardId);
        });

        view.findViewById(R.id.bt_delete_d1pay_card_details).setOnClickListener(v -> {
            showProgressDialog("Operation in progress.");
            mViewModel.deleteD1PayDigitalCard(mCardId);
        });

        view.findViewById(R.id.bt_set_default_d1pay_card).setOnClickListener(v -> {
            showProgressDialog("Operation in progress.");
            mViewModel.setDefaultCard(mCardId);
        });

        view.findViewById(R.id.bt_un_set_default_d1pay_card).setOnClickListener(v -> {
            showProgressDialog("Operation in progress.");
            mViewModel.unSetDefaultCard(mCardId);
        });

        mViewModel.getIsOperationSuccesfull().observe(getViewLifecycleOwner(), isLogoutSuccessful -> {
            hideProgressDialog();
        });

        mViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            hideProgressDialog();
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        });

        mViewModel.mCardBackground.observe(getViewLifecycleOwner(), bitmap -> {
            mCardBackground.setBackground(new BitmapDrawable(getResources(), bitmap));
        });

        mViewModel.mIcon.observe(getViewLifecycleOwner(), bitmap -> {
            mIcon.setBackground(new BitmapDrawable(getResources(), bitmap));
        });

        mViewModel.mIsDeleteCardSuccess.observe(getViewLifecycleOwner(), isDeleteSuccess -> {
            hideProgressDialog();
            if (isDeleteSuccess) {
                popFromBackstack();
            }
        });

        return view;
    }

    @NonNull
    @Override
    protected D1PayDigitalCardViewModel createViewModel() {
        return new ViewModelProvider(this).get(D1PayDigitalCardViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();

        showProgressDialog("Retrieving card information.");

        mViewModel.getD1PayDigitalCard(mCardId);
        mViewModel.getCardImages(mCardId);
    }
}