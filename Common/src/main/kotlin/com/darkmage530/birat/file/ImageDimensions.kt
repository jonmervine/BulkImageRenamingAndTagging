package com.darkmage530.birat.file

import arrow.core.Either
import javax.imageio.ImageIO
import javax.imageio.stream.FileImageInputStream

data class Dimensions(val width: Int, val height: Int)

fun getDimensions(file: File) =
    Either.catch {
        ImageIO.getImageReadersBySuffix(file.fileReference.extension)
            .next().let { reader ->
                reader.input = FileImageInputStream(file.fileReference)
                Dimensions(
                    reader.getWidth(reader.minIndex),
                    reader.getHeight(reader.minIndex)
                )
            }
    }.mapLeft {
        ImageError.ImageDimensionError(it)
    }