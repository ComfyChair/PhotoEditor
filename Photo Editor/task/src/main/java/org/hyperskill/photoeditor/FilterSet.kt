package org.hyperskill.photoeditor

import android.graphics.Bitmap
import org.hyperskill.photoeditor.BrightnessFilter.applyBrightness
import org.hyperskill.photoeditor.ContrastFilter.applyContrast

data class FilterSet(val brightnessOffset: Int, val contrastOffset: Int)

internal fun FilterSet.applyTo(bitmap: Bitmap) : Bitmap {
    val first = bitmap.applyBrightness(this.brightnessOffset)
    val second = first.applyContrast(this.contrastOffset)
    return second
}


