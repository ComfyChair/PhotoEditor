package org.hyperskill.photoeditor

import android.graphics.Bitmap
import android.graphics.Color

object ContrastFilter {
    internal fun Bitmap.applyContrast(offset: Int) : Bitmap {
        val avgBright = getAvgBrightness(this)
        val alpha : Double = (255.0 + offset) / (255.0 - offset)
        val pixels = IntArray(width * height)
        var index: Int
        for (y in 0 until height) {
            for (x in 0 until width) {
                // get current index in 2D-matrix
                index = y * width + x
                val r = (alpha * (Color.red(this.getPixel(x, y)) - avgBright)
                                + avgBright)
                    .toInt().coerceIn(0, 255)
                val g = (alpha * (Color.green(this.getPixel(x, y)) - avgBright)
                        + avgBright)
                    .toInt().coerceIn(0, 255)
                val b = (alpha * (Color.blue(this.getPixel(x, y)) - avgBright)
                        + avgBright)
                    .toInt().coerceIn(0, 255)
                pixels[index] = Color.rgb(r, g, b)
            }
        }
        val outBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        outBitmap.setPixels(pixels, 0, width, 0, 0, width, height )
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