/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.virtualcardlist;

import android.view.View;

import com.thalesgroup.mobile.d1sample.ui.basecardlist.AbstractCardListFragment;
import com.thalesgroup.mobile.d1sample.ui.virtualcarddetail.VirtualCardDetailFragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Fragment for the list of virtual cards.
 */
public class VirtualCardListFragment extends AbstractCardListFragment<VirtualCardListViewModel> {
    /**
     * Creates a new instance of {@code VirtualCardFragment}.
     *
     * @return Instance of {@code VirtualCardFragment}.
     */
    public static VirtualCardListFragment newInstance() {
        return new VirtualCardListFragment();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void showFragment(@NonNull final String cardId) {
        showFragment(VirtualCardDetailFragment.newInstance(cardId), true);
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
    protected VirtualCardListViewModel createViewModel() {
        return new ViewModelProvider(this).get(VirtualCardListViewModel.class);
    }
}