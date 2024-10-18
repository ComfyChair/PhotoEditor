package org.hyperskill.photoeditor

import android.graphics.Bitmap
import android.graphics.Color
import org.hyperskill.photoeditor.ContrastFilter.applyContrast

object SaturationFilter {
    internal fun Bitmap.applySaturation(offset: Int) : Bitmap {
        val alpha : Double = (255.0 + offset) / (255.0 - offset)
        val pixels = IntArray(width * height)
        var index: Int
        for (y in 0 until height) {
            for (x in 0 until width) {
                // get current index in 2D-matrix
                index = y * width + x
                val thisPixel = this.getPixel(x, y)
                val rgbAvg = getRgbAvg(thisPixel)
                val r = (alpha * (Color.red(thisPixel) - rgbAvg) + rgbAvg)
                    .toInt().coerceIn(0, 255)
                val g = (alpha * (Color.green(thisPixel) - rgbAvg) + rgbAvg)
                    .toInt().coerceIn(0, 255)
                val b = (alpha * (Color.blue(thisPixel) - rgbAvg) + rgbAvg)
                    .toInt().coerceIn(0, 255)
                pixels[index] = Color.rgb(r, g, b)
            }
        }
        val outBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        outBitmap.setPixels(pixels, 0, width, 0, 0, width, height )
        return outBitmap
    }

    private fun getRgbAvg(pixel: Int): Int {
        return (Color.red(pixel) + Color.green(pixel) + Color.blue(pixel)) / 3
    }
}