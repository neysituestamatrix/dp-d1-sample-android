/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.d1paytransactioinhistory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thalesgroup.gemalto.d1.d1pay.TransactionRecord;
import com.thalesgroup.gemalto.d1.validation.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * List adapter for transaction history.
 */
public class TransactionHistoryRecyclerViewAdapter
        extends RecyclerView.Adapter<TransactionHistoryRecyclerViewAdapter.ViewHolder> {

    private final List<TransactionRecord> mValues;

    /**
     * Creates a new instance of {@code TransactionHistoryRecyclerViewAdapter}.
     *
     * @param items List of {@code TransactionRecord}.
     */
    public TransactionHistoryRecyclerViewAdapter(@NonNull final List<TransactionRecord> items) {
        super();
        mValues = items;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                                        .inflate(R.layout.fragment_item_transaction_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mId.setText(mValues.get(position).getID());
        holder.mAmount.setText(mValues.get(position).getDisplayAmount());
        holder.mMerchant.setText(mValues.get(position).getMerchantName());
        holder.mDate.setText(mValues.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * View holder.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDate;
        public final TextView mMerchant;
        public final TextView mId;
        public final TextView mAmount;

        /**
         * Creates a new instance of {@code ViewHolder}.
         *
         * @param view Associated view.
         */
        public ViewHolder(final View view) {
            super(view);
            mView = view;
            mDate = view.findViewById(R.id.tv_date);
            mMerchant = view.findViewById(R.id.tv_merchant);
            mId = view.findViewById(R.id.tv_id);
            mAmount = view.findViewById(R.id.tv_amount);
        }

        @NotNull
        @Override
        public String toString() {
            return super.toString() + " '" + mId.getText() + "'";
        }
    }
}