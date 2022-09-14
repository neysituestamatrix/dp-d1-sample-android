/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.jwt;

import android.util.Base64;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import androidx.annotation.NonNull;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * Helper class for JWT generation.
 */
public class JWTEncoder {

    public static final String JWT_CLAIMS_SUB = "testuser";

    public static final String PRIVATE_KEY_ALGO_RSA = "RSA";
    public static final String PRIVATE_KEY_ALGO_EC = "EC";

    /**
     * Creates the private key from the PKCS-8 String.
     *
     * @param pkcs8Spec PKCS-8 String.
     * @param algo      Algorithm.
     * @return Private key.
     */
    public static PrivateKey readPrivateKey(@NonNull final String pkcs8Spec, @NonNull final String algo) {
        final String txtKey = pkcs8Spec.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "")
                                       .replace("\n", "");
        final byte[] rawKey = Base64.decode(txtKey, Base64.DEFAULT);

        final PKCS8EncodedKeySpec kSpec = new PKCS8EncodedKeySpec(rawKey);

        PrivateKey prvKey = null;
        try {
            final KeyFactory keyFactory = KeyFactory.getInstance(algo);
            prvKey = keyFactory.generatePrivate(kSpec);
        } catch (final NoSuchAlgorithmException | InvalidKeySpecException exception) {
            // nothing to do - just return null
        }

        return prvKey;
    }

    /**
     * Generates an A2 BAn token for a specific {@code customerID}
     *
     * @param tenant      Tenant
     * @param consumerID  The customer ID
     * @param extraClaims optional claims key-value to override
     * @return The A2 BAn token
     */
    public static String generateA2BAn(@NonNull final Tenant tenant,
                                       @NonNull final String consumerID,
                                       final Map<String, Object>... extraClaims) {
        final Claims claims = getClaims(tenant.getScope(),
                                        consumerID,
                                        "https://bpsd1-demo-a2oidc.d1-dev.thalescloud.io/oidc/" + tenant.getName(),
                                        tenant.getAudience(),
                                        getCurrentDate(),
                                        getExpiryDate());

        if (extraClaims != null && extraClaims.length > 0) {
            for (final Map<String, Object> claimMap : extraClaims) {
                if (claimMap != null) {
                    for (final Map.Entry<String, Object> entry : claimMap.entrySet()) {
                        claims.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }

        final Key key = tenant.getPrivateKey();
        return Jwts.builder().setHeaderParam("kid", tenant.getJwtKeyID()).setClaims(claims).signWith(key, tenant.getAlgo())
                   .compact();


        // Unable to load class named [io.jsonwebtoken.impl.DefaultClaims] from the thread context, current, or
        // system/application ClassLoaders.  All heuristics have been exhausted.  Class could not be found.  Have you
        // remembered to include the jjwt-impl.jar in your runtime classpath?
    }

    /**
     * Retrieves the current date.
     *
     * @return Current date.
     */
    private static Date getCurrentDate() {
        final Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    /**
     * Retrieves the expiration date.
     *
     * @return Expiration date.
     */
    private static Date getExpiryDate() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        return cal.getTime();
    }

    /**
     * Retrieves the Claims.
     *
     * @return Claims.
     */
    private static Claims getClaims(@NonNull final String scope,
                                    @NonNull final String sub,
                                    @NonNull final String iss,
                                    @NonNull final String aud,
                                    @NonNull final Date iat,
                                    @NonNull final Date exp) {
        final Claims claims = Jwts.claims().setSubject(sub).setIssuer(iss).setAudience(aud).setExpiration(exp)
                                  .setIssuedAt(iat);
        claims.put("scope", scope);
        return claims;
    }
}
