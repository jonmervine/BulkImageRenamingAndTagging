package com.darkmage530.birat

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ConfigTest : StringSpec({

    "fdjerajkldf" {
        Config.getUsername().shouldBe("birat")
        Config.getPassword().shouldBe("birat")
        Config.getTokenNote().shouldBe("BIRAT Uploads")
        Config.getExpirationTime().shouldBe(6L)
    }

})
