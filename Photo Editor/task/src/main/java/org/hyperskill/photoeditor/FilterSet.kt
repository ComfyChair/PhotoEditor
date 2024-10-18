package org.hyperskill.photoeditor

import android.graphics.Bitmap
import org.hyperskill.photoeditor.BrightnessFilter.applyBrightness
import org.hyperskill.photoeditor.ContrastFilter.applyContrast
import org.hyperskill.photoeditor.GammaFilter.applyGamma
import org.hyperskill.photoeditor.SaturationFilter.applySaturation

data class FilterSet(var brightnessOffset: Int,
                     var contrastOffset: Int,
                     var saturationOffset: Int,
                     var gammaValue: Double) {
    companion object {
        val Default = FilterSet(0,0,0,1.0)
    }
}

internal fun FilterSet.applyTo(bitmap: Bitmap) : Bitmap {
    val bright = bitmap.applyBrightness(this.brightnessOffset)
    val contrast = bright.applyContrast(this.contrastOffset)
    val saturated = contrast.applySaturation(this.saturationOffset)
    val gamma = saturated.applyGamma(this.gammaValue)
    return gamma
}


