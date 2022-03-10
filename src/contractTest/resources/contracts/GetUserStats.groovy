package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url '/user-stats'
        headers {
            accept('application/json')
            header("Authorization", "Bearer eyJlbmMiOiJBMTI4R0NNIiwiYWxnIjoiZGlyIn0..cRHGsWBM3Fkrpt4E.6OozLAfwDWdMWP7W0tNpuI_hQl1j2kubru0eyUZE9LtbZXXcJLIee-Pvwv3rlfCi5s2P9_IDr8oI-ke95B-rM1AFWsvOHlUB23jcPL8X3ARWISyLaPWRZglBp2yrS7k7lDAzkwg6s52sPQ.F1jWA0pPp9wAYrf041ez1A")
        }
    }
    response {
        status OK()
        headers {
            contentType('application/json')
        }
        body([
          "todoLists": [
            "created": 1,
            "limit"  : 5
          ]
        ])
    }
}