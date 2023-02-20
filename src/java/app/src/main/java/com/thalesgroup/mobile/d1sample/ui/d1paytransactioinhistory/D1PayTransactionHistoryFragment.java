package com.thalesgroup.mobile.d1sample.ui.d1paytransactioinhistory;/*
 * MIT License
 *
 * Copyright (c) 2020 Thales DIS
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.thalesgroup.gemalto.d1.d1pay.TransactionRecord;
import com.thalesgroup.gemalto.d1.validation.R;
import com.thalesgroup.mobile.d1sample.ui.base.AbstractBaseFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Transaction history Fragment.
 */
public class D1PayTransactionHistoryFragment extends AbstractBaseFragment<D1PayTransactionHistoryViewModel> {
    private String mCardId;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mCardId = getArguments().getString(ARG_CARD_ID);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_transaction_history, container, false);
        final RecyclerView recyclerView = view.findViewById(R.id.list);

        mViewModel.getTransactionHistory().observe(getViewLifecycleOwner(), new Observer<List<TransactionRecord>>() {
            @Override
            public void onChanged(final List<TransactionRecord> transactionRecords) {
                hideProgressDialog();
                final Context context = view.getContext();
                recyclerView.setLayoutManager(new LinearLayoutManager(context));

                final TransactionHistoryRecyclerViewAdapter adapter = new TransactionHistoryRecyclerViewAdapter(
                        transactionRecords);
                recyclerView.setAdapter(adapter);
            }
        });

        mViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            hideProgressDialog();
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        });

        showProgressDialog("Operation in Progress.");
        mViewModel.retrieveTransactionHistory(mCardId);

        return view;
    }

    /**
     * Creates a new instance of {@code D1PayTransactionHistoryFragment}.
     *
     * @return Instance of {@code D1PayTransactionHistoryFragment}.
     */
    public static D1PayTransactionHistoryFragment newInstance(@NonNull final String cardId) {
        final D1PayTransactionHistoryFragment fragment = new D1PayTransactionHistoryFragment();
        final Bundle args = new Bundle();
        args.putString(ARG_CARD_ID, cardId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    protected D1PayTransactionHistoryViewModel createViewModel() {
        return new ViewModelProvider(this).get(D1PayTransactionHistoryViewModel.class);
    }
}
