# [Samples] To do list app

Sample project showing my coding skills.

## Project overview

### Requirements for application

1. Should be production ready as much as possible
2. Should use JWE token to pass a user's authentication information
3. Should use hexagonal architecture
4. Should be written using TDD
5. Should have full spectrum of tests: architecture, unit, integration, contract, component

### Required features

1. Creating todo list by user
2. Managing (updating & deleting) todo lists by user
3. Listing todo lists
4. Viewing single todo list
5. Limiting the number of created todo lists by each user. At this moment each user can create only 5 todo lists.
6. Getting user statistics (number of created lists and max todo lists count)

### Constraints

1. List name must have at least 3 characters and no more than 50.
2. List can be empty but cannot have more than 25 tasks.
3. Task name must have at least 3 characters and no more than 150.

## Running and testing

### Authorization
This application uses JWE tokens ([RFC7516](https://datatracker.ietf.org/doc/html/rfc7516)) to pass authenticated user's data. This way of securing applications is much safer than using JWT, because there is no way to read internal data without knowing secret key.

To make requests authorized, JWE token must be put in "Authorization" header using the "Bearer" scheme ([RFC6750, section 2.1](https://datatracker.ietf.org/doc/html/rfc6750#section-2.1)).

For example:
```shell
$ curl -H "Authorization: Bearer eyJlbmMiOiJBMTI4R0NNIiwiYWxnIjoiZGlyIn0..cRHGsWBM3Fkrpt4E.6OozLAfwDWdMWP7W0tNpuI_hQl1j2kubru0eyUZE9LtbZXXcJLIee-Pvwv3rlfCi5s2P9_IDr8oI-ke95B-rM1AFWsvOHlUB23jcPL8X3ARWISyLaPWRZglBp2yrS7k7lDAzkwg6s52sPQ.F1jWA0pPp9wAYrf041ez1A" http://localhost:8080/secured-resource
```

### Test tokens

In the table below, you can find few JWE tokens for testing purposes. In real scenario, these tokens should be generated
by authentication server.

| UUID                                  | Username   | Expiration time       | JWE Token                                                                                                                                                                                                                        |
|---------------------------------------|------------|-----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| d003065b-adad-426a-8e8b-befe5bd47527  | jkowalski  | 2100-01-01T00:00:00Z  | eyJlbmMiOiJBMTI4R0NNIiwiYWxnIjoiZGlyIn0..cRHGsWBM3Fkrpt4E.6OozLAfwDWdMWP7W0tNpuI_hQl1j2kubru0eyUZE9LtbZXXcJLIee-Pvwv3rlfCi5s2P9_IDr8oI-ke95B-rM1AFWsvOHlUB23jcPL8X3ARWISyLaPWRZglBp2yrS7k7lDAzkwg6s52sPQ.F1jWA0pPp9wAYrf041ez1A  |
| 9bf5eb14-7aee-44c6-9a4e-3310d1da8a54  | jnowak     | 2100-01-01T00:00:00Z  | eyJlbmMiOiJBMTI4R0NNIiwiYWxnIjoiZGlyIn0..bEpRINka6tf8jVox.1eQhF02Fs5adSPad4soKuX1vL0n2z1J1_1DtpCZXaUKk86CGScV-uiSi-kSd3qFIKlgpA8JKpQCgydygus0htxOrrb1md-KwUEbhxl5m-01MHAavbVADCyt75zhiN0Tj7SHc2hX4KQ.MEFFjGmgEhInQ76bQfI0pw      |
| b3d53502-858c-46d1-ae02-6a502949400a  | jbravo     | 2100-01-01T00:00:00Z  | eyJlbmMiOiJBMTI4R0NNIiwiYWxnIjoiZGlyIn0..U3di2TqTFf3PBTO9.2rdNMW-qjbG8fnsMKdpjFxOYOjp7166FT3NWMWfvDhMuyuRScNH1U53WWN1xkBj_GcXTZMEKJYe2keWRg1NjFU8Ydqkr8lfXAyeDE_-mD2GA-vQEJrC7l1tYmW-pRZy5Fz5hxY00xA.VRkMueg6pOhsZR0Ij0elnQ      |
