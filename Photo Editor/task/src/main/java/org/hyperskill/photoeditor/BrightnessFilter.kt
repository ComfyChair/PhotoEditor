package org.hyperskill.photoeditor

import android.graphics.Bitmap
import android.graphics.Color
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive

object BrightnessFilter {
    internal suspend fun Bitmap.applyBrightness(offset: Int) : Bitmap {
        val outBitmap = this.copy(Bitmap.Config.RGB_565, true)
        val height = outBitmap.height
        val width = outBitmap.width
        coroutineScope {
            for (y in 0 until height) {
                if (isActive) {
                    for (x in 0 until width) {
                        if (isActive) {
                            val thisPixel = this@applyBrightness.getPixel(x, y)
                            val r = (Color.red(thisPixel) + offset)
                                .coerceIn(0, 255)
                            val g = (Color.green(thisPixel) + offset)
                                .coerceIn(0, 255)
                            val b = (Color.blue(thisPixel) + offset)
                                .coerceIn(0, 255)
                            outBitmap.setPixel(x, y, Color.rgb(r, g, b))
                        }
                    }
                }

            }
        }
        return outBitmap
    }
}