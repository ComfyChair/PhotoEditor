package org.hyperskill.photoeditor

import android.graphics.Bitmap
import android.graphics.Color
import kotlin.math.pow

object GammaFilter {
    internal fun Bitmap.applyGamma(gamma: Double) : Bitmap {
        val pixels = IntArray(width * height)
        var index: Int
        for (y in 0 until height) {
            for (x in 0 until width) {
                // get current index in 2D-matrix
                index = y * width + x
                val thisPixel = this.getPixel(x, y)
                val r = (255 * (Color.red(thisPixel) / 255.0).pow(gamma))
                    .toInt().coerceIn(0, 255)
                val g = (255 * (Color.green(thisPixel) / 255.0).pow(gamma))
                    .toInt().coerceIn(0, 255)
                val b = (255 * (Color.blue(thisPixel) / 255.0).pow(gamma))
                    .toInt().coerceIn(0, 255)
                pixels[index] = Color.rgb(r, g, b)
            }
        }
        val outBitmap = Bitmap.createBitmap(width, height, config)
        outBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return outBitmap
    }
}