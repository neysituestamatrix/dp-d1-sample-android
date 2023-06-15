/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.ui.base;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.thalesgroup.gemalto.d1.D1Exception;
import com.thalesgroup.gemalto.d1.D1Task;
import com.thalesgroup.gemalto.d1.card.AssetContent;
import com.thalesgroup.gemalto.d1.card.CardAsset;
import com.thalesgroup.gemalto.d1.card.CardMetadata;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Base ViewModel.
 */
public class BaseViewModel extends ViewModel {
    public final MutableLiveData<String> mErrorMessage = new MutableLiveData<>();
    public final MutableLiveData<Boolean> mIsOperationSuccesfull = new MutableLiveData<>(false);
    public MutableLiveData<Bitmap> mCardBackground = new MutableLiveData<>();
    public MutableLiveData<Bitmap> mIcon = new MutableLiveData<>();

    /**
     * Gets the error message live mutable data.
     *
     * @return Error message live mutable data
     */
    public MutableLiveData<String> getErrorMessage() {
        return mErrorMessage;
    }

    /**
     * Gets the mutable live data to indicate if log in was successful.
     *
     * @return Mutable live data.
     */
    public MutableLiveData<Boolean> getIsOperationSuccesfull() {
        return mIsOperationSuccesfull;
    }

    /**
     * Extracts the image resources.
     *
     * @param cardMetadata Card meta data.
     */
    protected void extractImageResources(final CardMetadata cardMetadata) {
        cardMetadata.getAssetList(new D1Task.Callback<List<CardAsset>>() {
            @Override
            public void onSuccess(final List<CardAsset> cardAssets) {
                if (cardAssets == null) {
                    return;
                }

                for (final CardAsset cardAsset : cardAssets) {
                    final CardAsset.AssetType assetType = cardAsset.getType();

                    for (final AssetContent assetContent : cardAsset.getContents()) {
                        if (assetContent.getMimeType() == AssetContent.MimeType.PNG) {
                            final byte[] data = Base64.decode(assetContent.getEncodedData(), Base64.DEFAULT);
                            final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            if (assetType == CardAsset.AssetType.CARD_BACKGROUND) {
                                mCardBackground.postValue(bitmap);
                            } else {
                                mIcon.postValue(bitmap);
                            }
                        } else if (assetContent.getMimeType() == AssetContent.MimeType.SVG) { // NOPMD - TODO
                            // TODO
                        } else if (assetContent.getMimeType() == AssetContent.MimeType.PDF) { // NOPMD - TODO
                            // TODO
                        }
                    }
                }
            }

            @Override
            public void onError(@NonNull final D1Exception exception) {
                mErrorMessage.postValue(exception.getLocalizedMessage());
            }
        });
    }
}
