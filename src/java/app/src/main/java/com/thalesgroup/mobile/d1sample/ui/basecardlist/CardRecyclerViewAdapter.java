/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.basecardlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thalesgroup.gemalto.d1.validation.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * List adapter for the card list.
 */
public class CardRecyclerViewAdapter extends RecyclerView.Adapter<CardRecyclerViewAdapter.ViewHolder> {

    private final List<String> mValues;
    private OnCardClickListener mOnCardClickListener;

    public CardRecyclerViewAdapter(@NonNull final List<String> items) {
        super();
        mValues = items;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mIdView.setText(String.format(Locale.ENGLISH, "%d", position));
        holder.mContentView.setText(mValues.get(position));
        holder.mContentView.setOnClickListener(v -> {
            if (mOnCardClickListener != null) {
                mOnCardClickListener.onCardClicked(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * Sets the on card click listener.
     *
     * @param onCardClickListener Listener.
     */
    public void setOnCardClickListener(@NonNull final OnCardClickListener onCardClickListener) {
        mOnCardClickListener = onCardClickListener;
    }

    /**
     * On Card click listener.
     */
    public interface OnCardClickListener {

        /**
         * On card clicked.
         *
         * @param view     View.
         * @param position Position in list.
         */
        void onCardClicked(final View view, final int position);
    }

    /**
     * View holder.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;

        public ViewHolder(final View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @NotNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}