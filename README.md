# D1 SDK Sample Android application

Sample application to show the integration of D1 SDK in to an Android application.

## Get started

To be able to build and run the sample application:

1. D1 SDK needs to be added to the sample application.
2. Application configuration needs to be updated.

Please contact your Thales representative to recieve D1 SDK and a working configuration.

### D1 SDK

Please refer to the sample application `build.gradle` files for the correct location of D1 SDK.

**`build.gradle`**
```groovy
allprojects {
    repositories {
        google()
        jcenter()
        flatDir{
            dirs 'libs'
        }
    }
}
```

**`app/build.gradle`**
```groovy
releaseImplementation(name: 'd1-release', ext: 'aar')
debugImplementation(name: 'd1-debug', ext: 'aar')
```

```bash
app/
├── build.gradle
├── proguard-rules.pro
├── src
└── libs
    ├── d1-debug.aar
    └── d1-release.aar
```

For more details, please refer to the [D1 SDK Integration](https://thales-dis-dbp.stoplight.io/docs/d1-developer-portal/branches/main/e984dad0fca5e-sdk-integration-on-android) section of the D1 Developer Portal.

### Configuration

#### D1 backend configuration

The D1 backend configuration needs to be updated in the following file:

**`app/src/main/java/com/thalesgroup/mobile/d1sample/sdk/Configuration.java`**
```java
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
}
```

For more details, please refer to the [D1 SDK Setup](https://thales-dis-dbp.stoplight.io/docs/d1-developer-portal/branches/main/ZG9jOjI4ODMzMjkz-onboarding) section of the D1 Developer Portal.

#### Authentication

To receive access to all D1 services, the user needs to authenticate with D1. This authentication is done using a [JSON Web Token (JWT)](https://auth0.com/docs/secure/tokens/json-web-tokens). For simplicity this token is generated in the sample application. To generate the JWT the following configuration needs to be updated:

**`app/src/main/java/com/thalesgroup/mobile/d1sample/jwt/Tenant.java`**
```java

/**
 * Tenant configuration.
 */
public class Tenant {

    public static final Tenant SANDBOX = new Tenant("name",
                                                    "scope",
                                                    "audience",
                                                    "jwtKeyId",
                                                    SignatureAlgorithm.ES256,
                                                    "privateKey");

}
```

For more details, please refer to the [D1 SDK Login](https://thales-dis-dbp.stoplight.io/docs/d1-developer-portal/branches/main/70d2f0c3dbfd9-login) section of the D1 Developer Portal.


#### D1Pay configuration

To use [D1Pay](https://thales-dis-dbp.stoplight.io/docs/d1-developer-portal/branches/main/0a7cb56c00838-d1-pay) services, the following configurations need to be updated:

* Google services
* Android keystore
* D1Pay backend configuration

##### Google services

[D1Pay](https://thales-dis-dbp.stoplight.io/docs/d1-developer-portal/branches/main/0a7cb56c00838-d1-pay) services use [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging). For this reason the corresponding `google-services.json` file needs to be added to the sample application.

##### Android keystore

To use [D1Pay](https://thales-dis-dbp.stoplight.io/docs/d1-developer-portal/branches/main/0a7cb56c00838-d1-pay) services, the sample application needs to be signed with a specific keystore. 

##### D1Pay backend configuration

The following files need to be updated with a working D1Pay backend configuration:

**`app/src/main/assets/gemcbp.properties`**
```bash

# The URL of the CPS server.
# To be provided by Thales integrator.
CPS_URL=https://dummy.endpoint.test/mobile/cps

# Server connection timeout in milliseconds.
CPS_CONNECTION_TIMEOUT=90000

# Read timeout for the HTTP input stream in milliseconds.
CPS_READ_TIMEOUT=90000

# Number of retries CPS Connection can perform.
# This is an Integer value and is provided by application developer.
# Default value is set to 5. This value is optional.
CPS_CONNECTION_RETRY_COUNT=5

# Retry connection timeout in milliseconds.
# Default value is set to 10000 and is provided by the application developer.
# This value is optional.
CPS_CONNECTION_RETRY_INTERVAL=10000
```

**`app/src/main/assets/mobilegateway.properties`**
```bash

# The URL of the MG server.
# To be provided by Thales integrator.
MG_CONNECTION_URL=https://dummy.endpoint.test/mobile/mg

# Wallet provider id. To be provided by Thales integrator
WALLET_PROVIDER_ID=WalletProviderId

# optional field.
# An ID which identifies the mobile application uniquely.
# This applies in cases where there are different mobile applications for the same provider.
# To be provided by Thales integrator.
WALLET_APPLICATION_ID=myApplicationId

# Server connection timeout in milliseconds.
MG_CONNECTION_TIMEOUT=30000

# Read timeout for the HTTP input stream in milliseconds.
MG_CONNECTION_READ_TIMEOUT=30000

# Number of retries MG Connection can perform.
# This is an Integer value and is provided by application developer.
MG_CONNECTION_RETRY_COUNT=3

# Retry connection timeout in milliseconds.
MG_CONNECTION_RETRY_INTERVAL=10000

# The URL of the MG server.
# To be provided by Thales integrator. (not used for now)
MG_TRANSACTION_HISTORY_CONNECTION_URL=https://dummy.endpoint.test/mobile/mg
```

**`app/src/main/assets/rages.properties`**
```bash

# A fixed parameter
REALM=CBP

OAUTH_CONSUMER_KEY=DUMMY_OAUTH_KEY_LABEL

RAGES_GATEWAY_URL=https://dummy.endpoint.test/rest/1.0

# Connection timeout in milliseconds.
RAGES_CONNECTION_TIMEOUT=30000

# Used for Certificate Signing Request in component that secures the HTTPS calls.
# Any value can be set.
# Refer to your Thales integrator for the value to be set for your solution.
CSR_DOMAIN=MyCompany

# Used for Certificate Signing Request in component that secures the HTTPS calls.
# Any value can be set.
# Refer to your Thales integrator for the value to be set for your solution.
CSR_EMAIL=contact@mycompany.com

# Number of retries Rages Connection can perform.
# This is an Integer value and is provided by application developer.
# Default value is set to 5.
# This value is optional.
CPS_CONNECTION_RETRY_COUNT=5

# Retry connection timeout in milliseconds.
# Default value is set to 10000 and is provided by the application developer.
# This value is optional.
CPS_CONNECTION_RETRY_INTERVAL=10000
```

For more details, please refer to the [D1Pay Service Application Setup](https://thales-dis-dbp.stoplight.io/docs/d1-developer-portal/branches/main/6ef89ec169de9-application-setup) section of the D1 Developer Portal.

## Build and run project

After all of the configurations have been updated, the application can be build. Application can be build either using Android Studio, or from the command line.

```bash
>> ./gradlew assemble
>> adb install app/build/outputs/apk/debug/app-debug.apk
```

## Source code overview

Most of D1 SDK related source code is located in the following classes:

* `D1Helper` - most of D1 SDK logic.
* `Configuration` - D1 backend configuration.
* `Tenant` - JWT configuration.
* `D1PayFirebaseService` - Handling of push messages.

## Documentation

[D1 Developer portal](https://thales-dis-dbp.stoplight.io/docs/d1-developer-portal/branches/main/ZG9jOjE1MjEwNTMy-digital-first-d1-ux)


## Contributing

If you are interested in contributing to the D1 SDK Sample Android application, start by reading the [Contributing guide](/CONTRIBUTING.md).

## License

[LICENSE](/LICENSE)
