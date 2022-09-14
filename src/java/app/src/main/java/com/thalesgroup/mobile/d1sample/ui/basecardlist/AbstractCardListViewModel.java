/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.basecardlist;

import com.thalesgroup.mobile.d1sample.ui.base.BaseViewModel;

import java.util.List;

import androidx.lifecycle.MutableLiveData;

/**
 * Abstract ViewModel for the card list - this ViewModel is reused to retrieve both virtual and digital card ids.
 */
public abstract class AbstractCardListViewModel extends BaseViewModel {
    private final MutableLiveData<List<String>> mCardIds = new MutableLiveData<>();

    /**
     * Retrieves the card list from D1.
     */
    public abstract void retrieveCardIds();

    /**
     * Retrieves the header name.
     *
     * @return Header name.
     */
    public abstract String getHeader();

    /**
     * Gets the live mutable data for the card list.
     *
     * @return Mutable live data for card list.
     */
    public MutableLiveData<List<String>> getCardIds() {
        return mCardIds;
    }
}
