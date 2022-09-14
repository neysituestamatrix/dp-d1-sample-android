/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.digitalcardlist;

import com.thalesgroup.gemalto.d1.D1Exception;
import com.thalesgroup.gemalto.d1.D1Task;
import com.thalesgroup.gemalto.d1.card.DigitalCard;
import com.thalesgroup.mobile.d1sample.sdk.D1Helper;
import com.thalesgroup.mobile.d1sample.ui.basecardlist.AbstractCardListViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

/**
 * ViewModel for the virtual card list.
 */
public class DigitalCardListViewModel extends AbstractCardListViewModel {
    private final MutableLiveData<List<String>> mCardIds = new MutableLiveData<>();
    private final String mCardId;

    DigitalCardListViewModel(@NonNull final String cardId) {
        super();
        mCardId = cardId;
    }

    /**
     * {@inheritDoc}
     */
    public void retrieveCardIds() {
        D1Helper.getInstance().getDigitalCardListD1Push(mCardId, new D1Task.Callback<List<DigitalCard>>() {
            @Override
            public void onSuccess(final List<DigitalCard> data) {
                extractDigitalCardIds(data);
            }

            @Override
            public void onError(final D1Exception exception) {
                mErrorMessage.postValue(exception.getLocalizedMessage());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHeader() {
        return "Digital Cards";
    }

    /**
     * Gets the live mutable data for the card list.
     *
     * @return Mutable live data for card list.
     */
    public MutableLiveData<List<String>> getCardIds() {
        return mCardIds;
    }

    /**
     * Extracts the list of digital cards ids from the digital card list.
     * @param data Digital card list.
     */
    private void extractDigitalCardIds(final List<DigitalCard> data) {
        final List<String> digitalCardIds = new ArrayList<>();
        for (final DigitalCard digitalCard : data) {
            digitalCardIds.add(digitalCard.getCardID());
        }

        mCardIds.postValue(digitalCardIds);
    }
}
