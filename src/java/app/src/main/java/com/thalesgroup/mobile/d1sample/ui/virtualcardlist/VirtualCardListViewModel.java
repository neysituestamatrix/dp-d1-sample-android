/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.virtualcardlist;

import com.thalesgroup.mobile.d1sample.sdk.Configuration;
import com.thalesgroup.mobile.d1sample.ui.basecardlist.AbstractCardListViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

/**
 * ViewModel for the virtual card list.
 */
public class VirtualCardListViewModel extends AbstractCardListViewModel {
    private final MutableLiveData<List<String>> mCardIds = new MutableLiveData<>();

    /**
     * {@inheritDoc}
     */
    public void retrieveCardIds() {
        final List<String> virtualCardList = new ArrayList<>();
        virtualCardList.add(Configuration.cardId);
        mCardIds.postValue(virtualCardList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHeader() {
        return "Virtual Cards";
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
