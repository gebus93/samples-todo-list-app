package pl.thinkandcode.samples.todo.component;

public enum AuthenticatedUser {
    USER_1("eyJlbmMiOiJBMTI4R0NNIiwiYWxnIjoiZGlyIn0..cRHGsWBM3Fkrpt4E.6OozLAfwDWdMWP7W0tNpuI_hQl1j2kubru0eyUZE9LtbZXXcJLIee-Pvwv3rlfCi5s2P9_IDr8oI-ke95B-rM1AFWsvOHlUB23jcPL8X3ARWISyLaPWRZglBp2yrS7k7lDAzkwg6s52sPQ.F1jWA0pPp9wAYrf041ez1A"),
    USER_2("eyJlbmMiOiJBMTI4R0NNIiwiYWxnIjoiZGlyIn0..bEpRINka6tf8jVox.1eQhF02Fs5adSPad4soKuX1vL0n2z1J1_1DtpCZXaUKk86CGScV-uiSi-kSd3qFIKlgpA8JKpQCgydygus0htxOrrb1md-KwUEbhxl5m-01MHAavbVADCyt75zhiN0Tj7SHc2hX4KQ.MEFFjGmgEhInQ76bQfI0pw"),
    USER_3("eyJlbmMiOiJBMTI4R0NNIiwiYWxnIjoiZGlyIn0..U3di2TqTFf3PBTO9.2rdNMW-qjbG8fnsMKdpjFxOYOjp7166FT3NWMWfvDhMuyuRScNH1U53WWN1xkBj_GcXTZMEKJYe2keWRg1NjFU8Ydqkr8lfXAyeDE_-mD2GA-vQEJrC7l1tYmW-pRZy5Fz5hxY00xA.VRkMueg6pOhsZR0Ij0elnQ");
    private final String token;

    AuthenticatedUser(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
