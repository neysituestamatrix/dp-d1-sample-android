/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.jwt;

import java.security.PrivateKey;

import androidx.annotation.NonNull;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Tenant configuration.
 */
public class Tenant {

    private final String mName;
    private final String mScope;
    private final String mAudience;
    private final String mJwtKeyID;
    private final SignatureAlgorithm mAlgo;
    private final String mJwtPrivateKey;
    private PrivateKey mPrivateKey;

    /**
     * Creates a new {@code Tenant} instance.
     *
     * @param name          Tenant name.
     * @param scope         Scope.
     * @param audience      Audience.
     * @param jwtKeyID      JWT Key id.
     * @param algo          Algorithm
     * @param jwtPrivateKey Private key.
     */
    public Tenant(@NonNull final String name,
                  @NonNull final String scope,
                  @NonNull final String audience,
                  @NonNull final String jwtKeyID,
                  @NonNull final SignatureAlgorithm algo,
                  @NonNull final String jwtPrivateKey) {
        mName = name;
        mScope = scope;
        mAudience = audience;
        mJwtKeyID = jwtKeyID;
        mAlgo = algo;
        mJwtPrivateKey = jwtPrivateKey;
    }

    /**
     * Retrieves the name.
     *
     * @return Name.
     */
    public String getName() {
        return mName;
    }

    /**
     * Retrieves the scope.
     *
     * @return Scope.
     */
    public String getScope() {
        return mScope;
    }

    /**
     * Retrieves the audience.
     *
     * @return Audience.
     */
    public String getAudience() {
        return mAudience;
    }

    /**
     * Retrieves the key id.
     *
     * @return Key id.
     */
    public String getJwtKeyID() {
        return mJwtKeyID;
    }

    /**
     * Retrieves the algorithm.
     *
     * @return Algorithm.
     */
    public SignatureAlgorithm getAlgo() {
        return mAlgo;
    }

    /**
     * Retrieves the private key.
     *
     * @return Private key.
     */
    public String getJwtPrivateKey() {
        return mJwtPrivateKey;
    }

    /**
     * Retrieves the private key.
     *
     * @return Private key.
     */
    public PrivateKey getPrivateKey() {
        if (mPrivateKey != null) {
            return mPrivateKey;
        }
        String privateKeyAlgo = null;
        switch (mAlgo) {
            case ES256:
            case ES384:
            case ES512:
                privateKeyAlgo = JWTEncoder.PRIVATE_KEY_ALGO_EC;
                break;
            case RS256:
            case RS384:
            case RS512:
                privateKeyAlgo = JWTEncoder.PRIVATE_KEY_ALGO_RSA;
                break;
            default:
                throw new IllegalStateException("Unsupported algorithm");
        }
        mPrivateKey = JWTEncoder.readPrivateKey(mJwtPrivateKey, privateKeyAlgo);
        return mPrivateKey;
    }
}
