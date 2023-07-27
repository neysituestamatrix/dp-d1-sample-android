/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.virtualcarddetail;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.nfc.NfcAdapter;
import android.nfc.cardemulation.CardEmulation;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.thalesgroup.gemalto.d1.card.CardDigitizationState;
import com.thalesgroup.gemalto.d1.card.State;
import com.thalesgroup.gemalto.d1.d1pay.D1HCEService;
import com.thalesgroup.gemalto.d1.validation.R;
import com.thalesgroup.gemalto.d1.validation.databinding.FragmentCardDetailBinding;
import com.thalesgroup.mobile.d1sample.ui.base.AbstractBaseFragment;
import com.thalesgroup.mobile.d1sample.ui.d1paydigitalcard.D1PayDigitalCardFragment;
import com.thalesgroup.mobile.d1sample.ui.digitalcardlist.DigitalCardListFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

/**
 * Virtual card detail Fragment.
 */
public class VirtualCardDetailFragment extends AbstractBaseFragment<VirtualCardDetailViewModel> {
    private String mCardId;
    private ImageView mIcon;
    private ConstraintLayout mCardBackground;

    /**
     * Creates a new instance of {@code CardDetailFragment}.
     *
     * @param cardId Card ID.
     * @return Instance of {@code CardDetailFragment}.
     */
    public static VirtualCardDetailFragment newInstance(@NonNull final String cardId) {
        final VirtualCardDetailFragment fragment = new VirtualCardDetailFragment();
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
    protected VirtualCardDetailViewModel createViewModel() {
        return new ViewModelProvider(this).get(VirtualCardDetailViewModel.class);
    }

    @Override
    public View onCreateView(@NotNull final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        final FragmentCardDetailBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_card_detail, container, false);
        binding.setLifecycleOwner(this);
        binding.setMViewModel(mViewModel);

        final View view = binding.getRoot();

        mCardBackground = view.findViewById(R.id.cl_card_layout);
        mIcon = view.findViewById(R.id.iv_icon);

        view.findViewById(R.id.bt_get_card_details).setOnClickListener(v -> {
            showProgressDialog("Operation in progress.");
            mViewModel.getCardDetails(mCardId);
        });

        view.findViewById(R.id.bt_hide_card_details).setOnClickListener(v -> {
            showProgressDialog("Operation in progress.");
            mViewModel.hideCardDetails();
        });

        view.findViewById(R.id.bt_add_card).setOnClickListener(v -> {
            showProgressDialog("Card digitization.");
            mViewModel.digitizeCard(mCardId, requireActivity());
        });

        view.findViewById(R.id.bt_show_digital_cards).setOnClickListener(v -> {
            showFragment(DigitalCardListFragment.newInstance(mCardId), true);
        });

        view.findViewById(R.id.bt_show_d1pay_digital_card).setOnClickListener(v -> {
            showFragment(D1PayDigitalCardFragment.newInstance(mCardId), true);
        });

        view.findViewById(R.id.bt_add_card_nfc).setOnClickListener(v -> {
            // Check if device has HCE feature.
            final PackageManager packageManager = getActivity().getPackageManager();
            if (packageManager.hasSystemFeature(PackageManager.FEATURE_NFC_HOST_CARD_EMULATION)) {
                showProgressDialog("Operation in progress.");
                mViewModel.digitizeCardD1Pay(mCardId);
            } else {
                Toast.makeText(getActivity(), "Device is missing HCE feature.", Toast.LENGTH_LONG).show();
            }
        });

        initModel();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        showProgressDialog("Retrieving card information.");

        mViewModel.getCardMetadata(mCardId);
    }

    /**
     * Checks Tap&Pay settings - if application is set to be the default payment application.
     */
    private void checkTapAndPaySettings() {
        final CardEmulation cardEmulation = CardEmulation.getInstance(NfcAdapter.getDefaultAdapter(getActivity()));
        final ComponentName componentName = new ComponentName(getActivity(), D1HCEService.class.getCanonicalName());
        if (!cardEmulation.isDefaultServiceForCategory(componentName, CardEmulation.CATEGORY_PAYMENT)) {
            // set application to be the default payment application
            final Intent activate = new Intent();
            activate.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activate.setAction(CardEmulation.ACTION_CHANGE_DEFAULT);
            activate.putExtra(CardEmulation.EXTRA_SERVICE_COMPONENT, componentName);
            activate.putExtra(CardEmulation.EXTRA_CATEGORY, CardEmulation.CATEGORY_PAYMENT);
            requireActivity().startActivity(activate);
        }
    }

    /**
     * Sets up observers for the ViewModel.
     */
    private void initModel() {
        mViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            hideProgressDialog();
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        });

        mViewModel.mCardBackground.observe(getViewLifecycleOwner(), bitmap -> {
            hideProgressDialog();
            mCardBackground.setBackground(new BitmapDrawable(getResources(), bitmap));
        });

        mViewModel.mIcon.observe(getViewLifecycleOwner(), bitmap -> {
            hideProgressDialog();
            mIcon.setBackground(new BitmapDrawable(getResources(), bitmap));
        });

        mViewModel.mCardState.observe(getViewLifecycleOwner(), cardState -> {
            hideProgressDialog();
            if (cardState.equals(State.DELETED.toString())) {
                popFromBackstack();
            }
        });

        mViewModel.mCardDigitizationStateD1Pay.observe(getViewLifecycleOwner(), cardDigitizationState -> {
            if (cardDigitizationState == CardDigitizationState.DIGITIZED) {
                // if card is already digitized, check tap&pay settings
                hideProgressDialog();
                checkTapAndPaySettings();
            }
        });

        mViewModel.mDigitizationStartedOk.observe(getViewLifecycleOwner(), digitizationStartedOk -> {
            hideProgressDialog();
            if (digitizationStartedOk) {
                Toast.makeText(getActivity(), "Digitization started OK", Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), "Waiting for push message", Toast.LENGTH_LONG).show();

                mViewModel.mDigitizationStartedOk.postValue(false); // reset to prevent UI update when screen returns.
            }
        });

        mViewModel.mDigitizationFinishedOk.observe(getViewLifecycleOwner(), digitizationFinishedOk -> {
            hideProgressDialog();
            if (digitizationFinishedOk) {
                Toast.makeText(getActivity(), "Digitization finished OK", Toast.LENGTH_LONG).show();

                mViewModel.mDigitizationFinishedOk.postValue(false); // reset to prevent UI update when screen returns.
            }
        });

        mViewModel.getIsOperationSuccesfull().observe(getViewLifecycleOwner(), aBoolean -> hideProgressDialog());

        // mViewModel.isCardDigitized(mCardId);
        mViewModel.isCardDigitizedNfc(mCardId);
    }
}