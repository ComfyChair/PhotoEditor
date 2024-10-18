package org.hyperskill.photoeditor

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.slider.Slider
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private lateinit var currentImage: ImageView
    private lateinit var galleryBtn: Button
    private lateinit var brightnessSlider: Slider
    private lateinit var originalBitmap: Bitmap
    private var filterJob: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindViews()
        setListeners()

        //do not change this line
        currentImage.setImageBitmap(createBitmap())
        originalBitmap = createBitmap()
    }


    private fun bindViews() {
        currentImage = findViewById(R.id.ivPhoto)
        galleryBtn = findViewById(R.id.btnGallery)
        brightnessSlider = findViewById(R.id.slBrightness)
    }

    private fun setListeners() {
        galleryBtn.setOnClickListener {
            //cancel old jobs
            filterJob?.cancel()
            activityResultLauncher.launch(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
            )
        }
        brightnessSlider.addOnChangeListener { _, value, fromUser ->
            // cancel previous job to reduce lag
            println("onChange: $value")
            filterJob?.cancel()
            if (fromUser){
                lifecycleScope.launch (Dispatchers.Default) {
                    filterJob = applyBrightness(originalBitmap, value)
                }
            }
        }
    }

    private val activityResultLauncher =
        registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val photoUri = result.data?.data ?: return@registerForActivityResult
                // update ivPhoto with loaded image and get Bitmap to apply filters
                //currentImage.setImageURI(photoUri) // this would only display the image
                brightnessSlider.value = 0.0f // reset slider position
                contentResolver.openInputStream(photoUri).use {
                    originalBitmap = BitmapFactory.decodeStream(it)
                }
                println("Loading new picture, dims: ${originalBitmap.height}x${originalBitmap.width}")
                currentImage.setImageBitmap(originalBitmap)
            }
        }

    private fun applyBrightness(inBitmap: Bitmap, offset: Float) : Job =
        lifecycleScope.launch(Dispatchers.Default) {
            //TODO: restrict size, improve cancellation and / or use streams to increase computation speed
            // large pics still lag
            val outBitmap = inBitmap.copy(Bitmap.Config.RGB_565, true)
            val height = outBitmap.height
            val width = outBitmap.width
            for (y in 0 until height) {
                if (isActive) {
                    for (x in 0 until width) {
                        if (isActive) {
                            val R = (Color.red(inBitmap.getPixel(x, y))
                                    + offset.toInt())
                                .coerceIn(0, 255)
                            val G = (Color.green(inBitmap.getPixel(x, y))
                                    + offset.toInt())
                                .coerceIn(0, 255)
                            val B = (Color.blue(inBitmap.getPixel(x, y))
                                    + offset.toInt())
                                .coerceIn(0, 255)
                            outBitmap.setPixel(x, y, Color.rgb(R, G, B))
                        }
                    }
                }
            }
            if (isActive) {
                println("Updating picture with brightness $offset")
                currentImage.setImageBitmap(outBitmap)
            }
        }

    // do not change this function
    fun createBitmap(): Bitmap {
        val width = 200
        val height = 100
        val pixels = IntArray(width * height)
        // get pixel array from source

        var R: Int
        var G: Int
        var B: Int
        var index: Int

        for (y in 0 until height) {
            for (x in 0 until width) {
                // get current index in 2D-matrix
                index = y * width + x
                // get color
                R = x % 100 + 40
                G = y % 100 + 80
                B = (x+y) % 100 + 120

                pixels[index] = Color.rgb(R,G,B)

            }
        }
        // output bitmap
        val bitmapOut = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        bitmapOut.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmapOut
    }

    override fun onDestroy() {
        super.onDestroy()
        filterJob?.cancel()
        filterJob = null
    }
}