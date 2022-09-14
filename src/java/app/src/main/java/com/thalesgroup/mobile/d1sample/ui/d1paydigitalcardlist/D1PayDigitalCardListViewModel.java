/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.d1paydigitalcardlist;

import com.thalesgroup.gemalto.d1.D1Exception;
import com.thalesgroup.gemalto.d1.D1Task;
import com.thalesgroup.gemalto.d1.d1pay.D1PayDigitalCard;
import com.thalesgroup.mobile.d1sample.sdk.Configuration;
import com.thalesgroup.mobile.d1sample.sdk.D1Helper;
import com.thalesgroup.mobile.d1sample.ui.basecardlist.AbstractCardListViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

/**
 * ViewModel for the virtual card list.
 */
public class D1PayDigitalCardListViewModel extends AbstractCardListViewModel {
    private final MutableLiveData<List<String>> mCardIds = new MutableLiveData<>();

    /**
     * {@inheritDoc}
     */
    public void retrieveCardIds() {
        D1Helper.getInstance().getDigitalCardListD1Pay(new D1Task.Callback<Map<String, D1PayDigitalCard>>() {
            @Override
            public void onSuccess(final Map<String, D1PayDigitalCard> stringD1PayDigitalCardMap) {
                if (stringD1PayDigitalCardMap != null && !stringD1PayDigitalCardMap.isEmpty()) {
                    final List<String> virtualCardList = new ArrayList<>(stringD1PayDigitalCardMap.keySet());
                    mCardIds.postValue(virtualCardList);
                } else {
                    mErrorMessage.postValue("No D1Pay digital cards available.");
                }
            }

            @Override
            public void onError(@NonNull final D1Exception exception) {
                mErrorMessage.postValue(exception.getLocalizedMessage());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHeader() {
        return "D1Pay Digital Cards";
    }

    /**
     * Gets the live mutable data for the card list.
     *
     * @return Mutable live data for card list.
     */
    public MutableLiveData<List<String>> getCardIds() {
        return mCardIds;
    }
}
