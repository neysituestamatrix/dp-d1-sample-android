/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.basecardlist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.thalesgroup.gemalto.d1.validation.R;
import com.thalesgroup.mobile.d1sample.ui.base.AbstractBaseFragment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Abstract list fragment - this list fragment is reused to display both virtual and digital cards.
 */
public abstract class AbstractCardListFragment<CLVM extends AbstractCardListViewModel> extends AbstractBaseFragment<CLVM> {
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        final RecyclerView recyclerView = view.findViewById(R.id.list);

        mViewModel.getCardIds().observe(getViewLifecycleOwner(), cardIds -> {
            hideProgressDialog();
            final Context context = view.getContext();
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            final CardRecyclerViewAdapter adapter = new CardRecyclerViewAdapter(cardIds);
            adapter.setOnCardClickListener((view1, position) -> {
                final String cardId = cardIds.get(position);
                showFragment(cardId);
            });

            recyclerView.setAdapter(adapter);
        });

        mViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            hideProgressDialog();
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        });

        ((TextView) view.findViewById(R.id.tv_header)).setText(mViewModel.getHeader());

        initOtherViews(view, recyclerView);

        showProgressDialog("Operation in Progress.");
        mViewModel.retrieveCardIds();

        return view;
    }

    /**
     * Shows the appropriate fragment after card ids have been loaded.
     *
     * @param cardId Card Id.
     */
    protected abstract void showFragment(@NonNull final String cardId);

    /**
     * Initialize other views etc. in sub classes.
     *
     * @param view View.
     * @param recyclerView RecyclerView.
     */
    protected abstract void initOtherViews(@NonNull final View view, @NonNull final RecyclerView recyclerView);
}