package org.hyperskill.photoeditor

import android.graphics.Bitmap
import android.graphics.Color
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive

object ContrastFilter {
    internal suspend fun Bitmap.applyContrast(offset: Int) : Bitmap {
        val outBitmap = this.copy(Bitmap.Config.RGB_565, true)
        coroutineScope {
            val avgBright = this@applyContrast.getAvgBrightness()
            val alpha : Double = (255.0 + offset) / (255.0 - offset)
            for (y in 0 until height) {
                if (isActive) {
                    for (x in 0 until width) {
                        if (isActive) {
                            val thisPixel = this@applyContrast.getPixel(x, y)
                            val r = (alpha * (Color.red(thisPixel) - avgBright) + avgBright)
                                .toInt().coerceIn(0, 255)
                            val g = (alpha * (Color.green(thisPixel) - avgBright) + avgBright)
                                .toInt().coerceIn(0, 255)
                            val b = (alpha * (Color.blue(thisPixel) - avgBright) + avgBright)
                                .toInt().coerceIn(0, 255)
                            outBitmap.setPixel(x, y, Color.rgb(r, g, b))
                        }
                    }
                }
            }
        }
        return outBitmap
    }

    private suspend fun Bitmap.getAvgBrightness(): Int {
        var totalBrightness = 0L
        val pixelCount = width * height
        coroutineScope {
            for (y in 0 until height) {
                if (isActive) {
                    for (x in 0 until width) {
                        if (isActive) {
                            val pixel = this@getAvgBrightness.getPixel(x, y)
                            totalBrightness += (Color.red(pixel) + Color.green(pixel) + Color.blue(pixel)) / 3
                        }
                    }
                }
            }
        }
        val avgBrightness : Int = (totalBrightness / pixelCount).toInt()
        return avgBrightness
    }
}