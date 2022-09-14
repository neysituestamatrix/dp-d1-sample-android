/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.d1paydigitalcardlist;

import android.view.View;

import com.thalesgroup.mobile.d1sample.ui.basecardlist.AbstractCardListFragment;
import com.thalesgroup.mobile.d1sample.ui.d1paydigitalcard.D1PayDigitalCardFragment;
import com.thalesgroup.mobile.d1sample.ui.login.LoginFragment;
import com.thalesgroup.mobile.d1sample.ui.virtualcarddetail.VirtualCardDetailFragment;
import com.thalesgroup.mobile.d1sample.ui.virtualcardlist.VirtualCardListViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Fragment for the list of d1pay digital cards.
 */
public class D1PayDigitalCardListFragment extends AbstractCardListFragment<D1PayDigitalCardListViewModel> {
    /**
     * Creates a new instance of {@code D1PayDigitalCardListFragment}.
     *
     * @return Instance of {@code D1PayDigitalCardListFragment}.
     */
    public static D1PayDigitalCardListFragment newInstance() {
        return new D1PayDigitalCardListFragment();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void showFragment(@NonNull final String cardId) {
        showFragment(D1PayDigitalCardFragment.newInstance(cardId), true);
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
    protected D1PayDigitalCardListViewModel createViewModel() {
        return new ViewModelProvider(this).get(D1PayDigitalCardListViewModel.class);
    }
}