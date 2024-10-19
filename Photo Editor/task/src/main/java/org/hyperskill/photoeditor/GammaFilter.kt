package org.hyperskill.photoeditor

import android.graphics.Bitmap
import android.graphics.Color
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlin.math.pow

object GammaFilter {
    internal suspend fun Bitmap.applyGamma(gamma: Double) : Bitmap {
        val outBitmap = this.copy(Bitmap.Config.RGB_565, true)
        coroutineScope {
            for (y in 0 until height) {
                if (isActive) {
                    for (x in 0 until width) {
                        if (isActive) {
                            val thisPixel = this@applyGamma.getPixel(x, y)
                            val r = (255 * (Color.red(thisPixel) / 255.0).pow(gamma))
                                .toInt().coerceIn(0, 255)
                            val g = (255 * (Color.green(thisPixel) / 255.0).pow(gamma))
                                .toInt().coerceIn(0, 255)
                            val b = (255 * (Color.blue(thisPixel) / 255.0).pow(gamma))
                                .toInt().coerceIn(0, 255)
                            outBitmap.setPixel(x, y, Color.rgb(r, g, b))
                        }
                    }
                }
            }
        }
        return outBitmap
    }
}