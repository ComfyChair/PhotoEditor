package org.hyperskill.photoeditor

import android.graphics.Bitmap
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
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

internal suspend fun FilterSet.applyTo(bitmap: Bitmap) : Bitmap  {
    var filtered = bitmap
    coroutineScope {
        if (isActive) {
            filtered = if (brightnessOffset != FilterSet.Default.brightnessOffset)
                filtered.applyBrightness(brightnessOffset).also {println("Applying brightness: $brightnessOffset")}
            else
                bitmap
        }
        if (isActive) {
            filtered = if (contrastOffset != FilterSet.Default.contrastOffset)
                filtered.applyContrast(contrastOffset).also { println("Applying contrast: $contrastOffset") }
            else
                filtered
        }
        if (isActive) {
            filtered = if (saturationOffset != FilterSet.Default.saturationOffset)
                filtered.applySaturation(saturationOffset).also { println("Applying saturation: $saturationOffset") }
            else
                filtered
        }
        if (isActive) {
            filtered = if (gammaValue != FilterSet.Default.gammaValue)
                filtered.applyGamma(gammaValue).also { println("Applying gamma: $gammaValue") }
            else
                filtered
        }
    }
    return filtered
}


