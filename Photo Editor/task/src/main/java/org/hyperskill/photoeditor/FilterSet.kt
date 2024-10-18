package org.hyperskill.photoeditor

import android.graphics.Bitmap
import org.hyperskill.photoeditor.BrightnessFilter.applyBrightness
import org.hyperskill.photoeditor.ContrastFilter.applyContrast
import org.hyperskill.photoeditor.GammaFilter.applyGamma
import org.hyperskill.photoeditor.SaturationFilter.applySaturation

data class FilterSet(val brightnessOffset: Int,
                     val contrastOffset: Int,
                     val saturationOffset: Int,
                     val gammaValue: Double) {
    companion object {
        val Default = FilterSet(0,0,0,1.0)
    }
}

internal fun FilterSet.applyTo(bitmap: Bitmap) : Bitmap {
    val bmBright = if (brightnessOffset != FilterSet.Default.brightnessOffset)
        bitmap.applyBrightness(brightnessOffset).also {println("Applying brightness: $brightnessOffset")}
     else
        bitmap
    val bmContrast = if (contrastOffset != FilterSet.Default.contrastOffset)
        bmBright.applyContrast(contrastOffset).also { println("Applying contrast: $contrastOffset") }
     else
        bmBright

    val bmSaturation = if (saturationOffset != FilterSet.Default.saturationOffset)
        bmContrast.applySaturation(saturationOffset).also { println("Applying saturation: $saturationOffset") }
     else
        bmContrast

    val bmGamma = if (gammaValue != FilterSet.Default.gammaValue)
        bmSaturation.applyGamma(gammaValue).also { println("Applying gamma: $gammaValue") }
     else
        bmSaturation

    return bmGamma
}


