package com.darkmage530.birat.file

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class ImageDimensionsKtTest : StringSpec({

    "Get dimensions for a png" {
        val file = java.io.File("src/test/resources/testpng.png")
        file.isFile.shouldBeTrue()
        getDimensions(File(file).getOrNull()!!).let {
            it.isRight().shouldBeTrue()
            it.getOrNull()!!.shouldBe(Dimensions(896, 1328))
        }
    }

    "Get dimensions for a jpg" {
        val file = java.io.File("src/test/resources/testjpg.jpg")
        file.isFile.shouldBeTrue()
        getDimensions(File(file).getOrNull()!!).let {
            it.isRight().shouldBeTrue()
            it.getOrNull()!!.shouldBe(Dimensions(1260, 2048))
        }
    }

    "Fail get dimensions on txt" {
        val file = File(java.io.File("src/test/resources/test.txt")).getOrNull()!!
        getDimensions(file).let {
            it.isLeft().shouldBeTrue()
            it.leftOrNull()!!.shouldBeInstanceOf<ImageError.ImageDimensionError>()
        }
    }
})
