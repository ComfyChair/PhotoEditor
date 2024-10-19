package org.hyperskill.photoeditor

import android.graphics.Bitmap
import android.graphics.Color
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive

object SaturationFilter {
    internal suspend fun Bitmap.applySaturation(offset: Int) : Bitmap {
        val outBitmap = this.copy(Bitmap.Config.RGB_565, true)
        val alpha : Double = (255.0 + offset) / (255.0 - offset)
        coroutineScope {
            for (y in 0 until height) {
                if (isActive) {
                    for (x in 0 until width) {
                        if (isActive) {
                            val thisPixel = this@applySaturation.getPixel(x, y)
                            val rgbAvg = getRgbAvg(thisPixel)
                            val r = (alpha * (Color.red(thisPixel) - rgbAvg) + rgbAvg)
                                .toInt().coerceIn(0, 255)
                            val g = (alpha * (Color.green(thisPixel) - rgbAvg) + rgbAvg)
                                .toInt().coerceIn(0, 255)
                            val b = (alpha * (Color.blue(thisPixel) - rgbAvg) + rgbAvg)
                                .toInt().coerceIn(0, 255)
                            outBitmap.setPixel(x, y, Color.rgb(r, g, b))
                        }
                    }
                }
            }
        }
        return outBitmap
    }

    private fun getRgbAvg(pixel: Int): Int {
        return (Color.red(pixel) + Color.green(pixel) + Color.blue(pixel)) / 3
    }
}