package org.hyperskill.photoeditor

import android.graphics.Bitmap
import android.graphics.Color
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive

object ContrastFilter {
    internal suspend fun Bitmap.applyContrast(offset: Int) : Bitmap {
        val outBitmap = this.copy(Bitmap.Config.RGB_565, true)
        coroutineScope {
            val avgBright = getAvgBrightness(this@applyContrast)
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

    private fun getAvgBrightness(bitmap: Bitmap): Int {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val avgBright = pixels
            .map {(Color.red(it) + Color.green(it) + Color.blue(it)) / 3 }
            .reduce {acc, i -> acc + i } / pixels.size
        return avgBright
    }
}