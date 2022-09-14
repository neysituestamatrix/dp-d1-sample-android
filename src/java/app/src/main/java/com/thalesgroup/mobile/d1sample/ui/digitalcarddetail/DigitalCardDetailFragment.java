/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.digitalcarddetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.thalesgroup.gemalto.d1.card.State;
import com.thalesgroup.gemalto.d1.validation.R;
import com.thalesgroup.gemalto.d1.validation.databinding.FragmentCardDigitalDetailBinding;
import com.thalesgroup.mobile.d1sample.ui.base.AbstractBaseFragment;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

/**
 * Digital card detail Fragment.
 */
public class DigitalCardDetailFragment extends AbstractBaseFragment<DigitalCardDetailViewModel> {

    private static final String ARG_CARD_ID = "ARG_CARD_ID";
    private static final String ARG_CARD_DIGITAL_ID = "ARG_CARD_DIGITAL_ID";

    private String mCardId;
    private String mDigitalCardId;

    /**
     * Creates a new instance of {@code CardDetailFragment}.
     *
     * @param cardId Card ID.
     * @return Instance of {@code CardDetailFragment}.
     */
    public static DigitalCardDetailFragment newInstance(@NonNull final String cardId, @NonNull final String digitalCardId) {
        final DigitalCardDetailFragment fragment = new DigitalCardDetailFragment();
        final Bundle args = new Bundle();
        args.putString(ARG_CARD_ID, cardId);
        args.putString(ARG_CARD_DIGITAL_ID, digitalCardId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mCardId = getArguments().getString(ARG_CARD_ID);
            mDigitalCardId = getArguments().getString(ARG_CARD_DIGITAL_ID);
        }
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    protected DigitalCardDetailViewModel createViewModel() {
        return new ViewModelProvider(this).get(DigitalCardDetailViewModel.class);
    }

    @Override
    public View onCreateView(@NotNull final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        final FragmentCardDigitalDetailBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_card_digital_detail, container, false);
        binding.setLifecycleOwner(this);
        binding.setMViewModel(mViewModel);

        final View view = binding.getRoot();

        view.findViewById(R.id.bt_suspend_card_details).setOnClickListener(v -> {
            showProgressDialog("Operation in progress.");
            mViewModel.suspendDigitalCard(mCardId);
        });

        view.findViewById(R.id.bt_resume_card_details).setOnClickListener(v -> {
            showProgressDialog("Operation in progress.");
            mViewModel.resumeDigitalCard(mCardId);
        });

        view.findViewById(R.id.bt_delete_card_details).setOnClickListener(v -> {
            showProgressDialog("Operation in progress.");
            mViewModel.deleteDigitalCard(mCardId);
        });

        mViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            hideProgressDialog();
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        });

        mViewModel.mCardState.observe(getViewLifecycleOwner(), cardState -> {
            hideProgressDialog();
            if (cardState.equals(State.DELETED.toString())) {
                popFromBackstack();
            }
        });

        mViewModel.getIsOperationSuccesfull().observe(getViewLifecycleOwner(), aBoolean -> hideProgressDialog());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        showProgressDialog("Retrieving card information.");

        mViewModel.getDigitalCardDetails(mCardId, mDigitalCardId);

    }
}