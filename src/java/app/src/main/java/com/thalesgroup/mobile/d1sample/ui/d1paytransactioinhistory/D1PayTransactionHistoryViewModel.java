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

import com.thalesgroup.gemalto.d1.D1Exception;
import com.thalesgroup.gemalto.d1.D1Task;
import com.thalesgroup.gemalto.d1.d1pay.TransactionHistory;
import com.thalesgroup.gemalto.d1.d1pay.TransactionRecord;
import com.thalesgroup.mobile.d1sample.sdk.D1Helper;
import com.thalesgroup.mobile.d1sample.ui.base.BaseViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

/**
 * Transaction history ViewModel.
 */
public class D1PayTransactionHistoryViewModel extends BaseViewModel {
    private final MutableLiveData<List<TransactionRecord>> mTransactionHistory = new MutableLiveData<>();

    /**
     * Retrieves the transaction history for give card ID.
     *
     * @param cardId Card ID.
     */
    public void retrieveTransactionHistory(@NonNull final String cardId) {
        D1Helper.getInstance().getD1PayTransactionHistory(cardId, new D1Task.Callback<TransactionHistory>() {
            @Override
            public void onSuccess(final TransactionHistory transactionHistory) {
                if (transactionHistory != null) {
                    mTransactionHistory.postValue(transactionHistory.getRecords());
                } else {
                    mErrorMessage.postValue("No transaction history.");
                }
            }

            @Override
            public void onError(@NonNull final D1Exception exception) {
                mErrorMessage.postValue(exception.getLocalizedMessage());
            }
        });
    }

    /**
     * Gets the transaction history mutable data.
     *
     * @return Transaction history mutable data.
     */
    public MutableLiveData<List<TransactionRecord>> getTransactionHistory() {
        return mTransactionHistory;
    }
}
