/*
 * Copyright © 2022 THALES. All rights reserved.
 */

package com.thalesgroup.mobile.d1sample.sdk;

import com.thalesgroup.mobile.d1sample.jwt.Tenant;

import io.jsonwebtoken.SignatureAlgorithm;

/**
 * D1 SDK configuration.
 */
public class Configuration {
    // The URL of D1 Service Server.
    public static final String D1_SERVICE_URL = "";

    // The issuer identifier.
    public static final String ISSUER_ID = "";

    // The RSA exponent of the public key for secure communication between D1 Service Server and the SDK.
    public static final String D1_SERVICE_RSA_EXPONENT = "";

    // The RSA modulus of the public key for secure communication between D1 Service Server and the SDK.
    public static final String D1_SERVICE_RSA_MODULUS = "";

    // The URL for digital card operation.
    public static final String DIGITAL_CARD_URL
            = "";

    // Consumer ID.
    public static final String CONSUMER_ID = "";

    // Card ID.
    public static final String CARD_ID = "";

    // JWT configuration for authentication.
    public static final Tenant SANDBOX = new Tenant("name",
                                                    "scope",
                                                    "audience",
                                                    "jwtKeyId",
                                                    SignatureAlgorithm.ES256,
                                                    "privateKey");
}
