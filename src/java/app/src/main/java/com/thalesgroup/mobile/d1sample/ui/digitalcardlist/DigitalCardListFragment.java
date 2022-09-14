/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.digitalcardlist;

import android.os.Bundle;
import android.view.View;

import com.thalesgroup.gemalto.d1.validation.R;
import com.thalesgroup.mobile.d1sample.ui.basecardlist.AbstractCardListFragment;
import com.thalesgroup.mobile.d1sample.ui.digitalcarddetail.DigitalCardDetailFragment;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Fragment for the list of digital cards.
 */
public class DigitalCardListFragment extends AbstractCardListFragment<DigitalCardListViewModel> {
    private static final String ARG_CARD_ID = "ARG_CARD_ID";

    private String mCardId;

    /**
     * Creates a new instance of {@code VirtualCardFragment}.
     *
     * @return Instance of {@code VirtualCardFragment}.
     */
    public static DigitalCardListFragment newInstance(@NonNull final String cardId) {
        final DigitalCardListFragment digitalCardListFragment = new DigitalCardListFragment();
        final Bundle args = new Bundle();
        args.putString(ARG_CARD_ID, cardId);
        digitalCardListFragment.setArguments(args);

        return digitalCardListFragment;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        if (getArguments() != null) {
            mCardId = getArguments().getString(ARG_CARD_ID);
        }

        super.onCreate(savedInstanceState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void showFragment(@NonNull final String digitalCardId) {
        showFragment(DigitalCardDetailFragment.newInstance(mCardId, digitalCardId), true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initOtherViews(@NonNull final View view, @NonNull final RecyclerView recyclerView) {
        // nothing to do
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    protected DigitalCardListViewModel createViewModel() {
        final ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
            @NotNull
            @Override
            public <T extends ViewModel> T create(@NotNull final Class<T> aClass) {
                return (T) new DigitalCardListViewModel(mCardId);
            }
        };

        return new ViewModelProvider(this, factory).get(DigitalCardListViewModel.class);
    }
}