/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.physicalcarddetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.thalesgroup.gemalto.d1.PINDisplayTextView;
import com.thalesgroup.gemalto.d1.SecureEditText;
import com.thalesgroup.gemalto.d1.validation.R;
import com.thalesgroup.gemalto.d1.validation.databinding.FragmentPhysicalCardDetailBinding;
import com.thalesgroup.mobile.d1sample.ui.base.AbstractBaseFragment;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

/**
 * Virtual card detail Fragment.
 */
public class PhysicalCardDetailFragment extends AbstractBaseFragment<PhysicalCardDetailViewModel> {

    private static final String ARG_CARD_ID = "ARG_CARD_ID";

    private String mCardId;

    /**
     * Creates a new instance of {@code CardDetailFragment}.
     *
     * @param cardId Card ID.
     * @return Instance of {@code CardDetailFragment}.
     */
    public static PhysicalCardDetailFragment newInstance(@NonNull final String cardId) {
        final PhysicalCardDetailFragment fragment = new PhysicalCardDetailFragment();
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

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    protected PhysicalCardDetailViewModel createViewModel() {
        return new ViewModelProvider(this).get(PhysicalCardDetailViewModel.class);
    }

    @Override
    public View onCreateView(@NotNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final FragmentPhysicalCardDetailBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_physical_card_detail, container, false);
        binding.setLifecycleOwner(this);
        binding.setMViewModel(mViewModel);

        final View view = binding.getRoot();
        final SecureEditText secureTextEdit = view.findViewById(R.id.ste_activation);

        final Button activateCard = view.findViewById(R.id.bt_activate_card);
        activateCard.setOnClickListener(v -> {
            mViewModel.activatePhysicalCard(mCardId, secureTextEdit);
        });

        final PINDisplayTextView pinDisplayTextView = view.findViewById(R.id.pdtv_pin);
        view.findViewById(R.id.bt_show_pin).setOnClickListener(v -> {
            showProgressDialog("Operation in progress.");
            mViewModel.getPhysicalCardPin(mCardId, pinDisplayTextView);
        });

        mViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            hideProgressDialog();
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        });

        mViewModel.getIsOperationSuccesfull().observe(getViewLifecycleOwner(), aBoolean -> {
            hideProgressDialog();
            Toast.makeText(getActivity(), "Operation success", Toast.LENGTH_LONG).show();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        showProgressDialog("Retrieving activation method");

        mViewModel.getActivationMethod(mCardId);
    }
}