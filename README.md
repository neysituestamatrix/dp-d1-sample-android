# D1 SDK Sample Android application

Sample application to show the integration of D1 SDK in to an Android application.

## Get started

To be able to build and run the sample application:

1. D1 SDK needs to be added to the sample application.
2. Application configuration needs to be added.

The following files need to be added to the project:

1. D1 backend configuration
   
```bash
src/java/app/src/main/assets/d1.properties
```

2. D1 SDK

```bash
src/java/app/libs
               ├── d1-debug.aar
               └── d1-release.aar
```

3. Keystore

```bash
src/java/app/keystore/keystore
```

```bash
src/java/app/src/main/assets/keystore.properties
```

4. D1Pay configuration

```bash
src/java/app/src/main/assets
                        ├── gemcbp.properties
                        ├── mobilegateway.properties
                        └── rages.properties

```

5. `google-services.json` file

```bash
src/java/app/google-services.json
```

Adding the Keystore and D1 Pay configuration is only mandatory if using D1Pay feature.

Please contact your Thales representative to recieve D1 SDK and a working configuration.

### D1 SDK

This sample application was tested with **D1 SDK version 3.0.0**.
Please refer to the sample application `build.gradle` files for the correct location of D1 SDK.

**`src/java/build.gradle`**
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

**`src/java/app/build.gradle`**
```groovy
releaseImplementation(name: 'd1-release', ext: 'aar')
debugImplementation(name: 'd1-debug', ext: 'aar')
```

```bash
src/java/app/
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

The `d1.properties` file which holds the D1 backend configuration needs to be added to the project:

**`src/java/app/src/main/assets/d1.properties`**
```bash
D1_SERVICE_URL = 
ISSUER_ID = 
D1_SERVICE_RSA_EXPONENT = 
D1_SERVICE_RSA_MODULUS = 
DIGITAL_CARD_URL = 
CONSUMER_ID = 
CARD_ID = 
SANDBOX_NAME = 
SANDBOX_SCOPE = 
SANDBOX_AUDIENCE = 
SANDBOX_KEY_ID = 
SANDBOX_ALGO = 
SANDBOX_PRIVATE_KEY =
```
The `d1.properties` file is not kept under version control to prevent it from being overwritten during repository update.


For more details, please refer to the [D1 SDK Setup](https://thales-dis-dbp.stoplight.io/docs/d1-developer-portal/branches/main/ZG9jOjI4ODMzMjkz-onboarding) section of the D1 Developer Portal.

#### Authentication

To receive access to all D1 services, the user needs to authenticate with D1. This authentication is done using a [JSON Web Token (JWT)](https://auth0.com/docs/secure/tokens/json-web-tokens). For simplicity this token is generated in the sample application. The JWT configuration is part of the `d1.properties` file.

For more details, please refer to the [D1 SDK Login](https://thales-dis-dbp.stoplight.io/docs/d1-developer-portal/branches/main/70d2f0c3dbfd9-login) section of the D1 Developer Portal.


#### D1Pay configuration

To use [D1Pay](https://thales-dis-dbp.stoplight.io/docs/d1-developer-portal/branches/main/0a7cb56c00838-d1-pay) services, the following configurations need to be added:

* Google services
* Android keystore
* D1Pay backend configuration

##### Google services

[D1Pay](https://thales-dis-dbp.stoplight.io/docs/d1-developer-portal/branches/main/0a7cb56c00838-d1-pay) services use [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging). For this reason the corresponding `google-services.json` file needs to be added to the sample application.

##### Android keystore

To use [D1Pay](https://thales-dis-dbp.stoplight.io/docs/d1-developer-portal/branches/main/0a7cb56c00838-d1-pay) services, the sample application needs to be signed with a specific keystore - [Mobile banking application signing key](https://thales-dis-dbp.stoplight.io/docs/d1-developer-portal/branches/main/4723a96dffe6e-onboarding#mobile-banking-application-signing-key). The sample application needs to be updated with the appropriate keystore and signing configuration.

**`src/java/app/build.gradle`**
```groovy

android {
    signingConfigs {
        signingD1Pay {
            // Load keystore
            def keystorePropertiesFile = rootProject.file("app/src/main/assets/keystore.properties");
            def keystoreProperties = new Properties()
            if (keystorePropertiesFile.canRead()) {
                keystoreProperties.load(new FileInputStream(keystorePropertiesFile))

                storeFile file(keystoreProperties['storeFile'])
                storePassword keystoreProperties['storePassword']
                keyAlias keystoreProperties['keyAlias']
                keyPassword keystoreProperties['keyPassword']
            }
        }
    }
}
```

**`src/java/app/src/main/assets/keystore.properties`**
```bash
storePassword= 
keyPassword= 
keyAlias= 
storeFile=keystore/keystore
```

##### D1Pay backend configuration

The following files with a working D1Pay backend configuration need to be added:

**`src/java/app/src/main/assets/gemcbp.properties`**
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

**`src/java/app/src/main/assets/mobilegateway.properties`**
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

**`src/java/app/src/main/assets/rages.properties`**
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

These files are not kept under version control to prevent them from being overwritten during repository update.

For more details, please refer to the [D1Pay Service Application Setup](https://thales-dis-dbp.stoplight.io/docs/d1-developer-portal/branches/main/6ef89ec169de9-application-setup) section of the D1 Developer Portal.

## Build and run project

After all of the configurations have been added, the application can be build. Application can be build either using Android Studio, or from the command line.

The application consists of 2 build flavors:

`virtualCard` - Virtual card use case.
`d1Pay` - D1Pay use case.

```bash
>> ./gradlew assembleVirtualCard
>> adb install  app/build/outputs/apk/virtualCard/debug/app-virtualCard-debug.apk
```

```bash
>> ./gradlew assembleVirtualCard
>> adb install  app/build/outputs/apk/d1Pay/debug/app-d1Pay-debug.apk
```

## Source code overview

Most of D1 SDK related source code is located in the following classes:

* `D1Helper` - most of D1 SDK logic.
* `Tenant` - JWT configuration.
* `D1PayFirebaseService` - Handling of push messages.

## Documentation

[D1 Developer portal](https://thales-dis-dbp.stoplight.io/docs/d1-developer-portal/branches/main/ZG9jOjE1MjEwNTMy-digital-first-d1-ux)


## Contributing

If you are interested in contributing to the D1 SDK Sample Android application, start by reading the [Contributing guide](/CONTRIBUTING.md).

## License

[LICENSE](/LICENSE)
