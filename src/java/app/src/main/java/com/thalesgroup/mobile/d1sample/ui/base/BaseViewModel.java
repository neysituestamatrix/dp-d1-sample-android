package com.thalesgroup.mobile.d1sample.ui.base;/*
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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.thalesgroup.gemalto.d1.card.AssetContent;
import com.thalesgroup.gemalto.d1.card.CardAsset;
import com.thalesgroup.gemalto.d1.card.CardMetadata;

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
        if (cardMetadata.getAssetList() == null) {
            return;
        }

        for (final CardAsset cardAsset : cardMetadata.getAssetList()) {
            final CardAsset.AssetType assetType = cardAsset.getType();

            if (cardAsset.getContents() == null) {
                continue;
            }

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
}
