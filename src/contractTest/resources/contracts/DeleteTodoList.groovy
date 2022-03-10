package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'DELETE'
        url '/todo-lists/b2865319-d026-4ab1-b94a-7a67db79c66a'
        headers {
            header("Authorization", "Bearer eyJlbmMiOiJBMTI4R0NNIiwiYWxnIjoiZGlyIn0..cRHGsWBM3Fkrpt4E.6OozLAfwDWdMWP7W0tNpuI_hQl1j2kubru0eyUZE9LtbZXXcJLIee-Pvwv3rlfCi5s2P9_IDr8oI-ke95B-rM1AFWsvOHlUB23jcPL8X3ARWISyLaPWRZglBp2yrS7k7lDAzkwg6s52sPQ.F1jWA0pPp9wAYrf041ez1A")
        }
    }
    response {
        status NO_CONTENT()
    }
}