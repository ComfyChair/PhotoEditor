package org.hyperskill.photoeditor

import android.graphics.Bitmap
import android.graphics.Color

object BrightnessFilter {
    internal fun Bitmap.applyBrightness(offset: Int) : Bitmap {
        val pixels = IntArray(width * height)
        var index: Int
        for (y in 0 until height) {
            for (x in 0 until width) {
                // get current index in 2D-matrix
                index = y * width + x
                val r = (Color.red(this.getPixel(x, y))
                        + offset)
                    .coerceIn(0, 255)
                val g = (Color.green(this.getPixel(x, y))
                        + offset)
                    .coerceIn(0, 255)
                val b = (Color.blue(this.getPixel(x, y))
                        + offset)
                    .coerceIn(0, 255)
                pixels[index] = Color.rgb(r, g, b)
            }
        }
        val outBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        outBitmap.setPixels(pixels, 0, width, 0, 0, width, height )
        return outBitmap
    }
}