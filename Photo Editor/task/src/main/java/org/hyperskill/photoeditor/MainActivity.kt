package org.hyperskill.photoeditor

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.material.slider.Slider

private const val PERMISSION_REQUEST_CODE = 0

class MainActivity : AppCompatActivity() {

    private lateinit var currentImage: ImageView
    private lateinit var galleryBtn: Button
    private lateinit var saveBtn: Button
    private lateinit var brightnessSlider: Slider
    private lateinit var contrastSlider: Slider
    private lateinit var originalBitmap: Bitmap
    private var filters : FilterSet = FilterSet(0,0)
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
        saveBtn = findViewById(R.id.btnSave)
        brightnessSlider = findViewById(R.id.slBrightness)
        contrastSlider = findViewById(R.id.slContrast)
    }

    private fun setListeners() {
        galleryBtn.setOnClickListener {
            activityResultLauncher.launch(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
            )
        }
        saveBtn.setOnClickListener {
            checkPermission()
        }
        brightnessSlider.addOnChangeListener { _, value, fromUser ->
            filters = FilterSet(value.toInt(), filters.contrastOffset)
            println("Filters: $filters")
            currentImage.setImageBitmap(filters.applyTo(originalBitmap))
        }
        contrastSlider.addOnChangeListener { _, value, fromUser ->
            filters = FilterSet(filters.brightnessOffset, value.toInt())
            println("Filters: $filters")
            currentImage.setImageBitmap(filters.applyTo(originalBitmap))
        }
    }

    private val activityResultLauncher =
        registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val photoUri = result.data?.data ?: return@registerForActivityResult
                // update ivPhoto with loaded image and get Bitmap to apply filters
                //currentImage.setImageURI(photoUri) // this would only display the image
                contentResolver.openInputStream(photoUri).use {
                    originalBitmap = BitmapFactory.decodeStream(it)
                }
                currentImage.setImageBitmap(originalBitmap)
                // reset slider positions and filter
                filters = FilterSet(0,0)
                brightnessSlider.value = 0.0f
                contrastSlider.value = 0.0f
            }
        }

    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED -> savePicture()
            ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) -> requestPermission()
            else -> requestPermission()
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this as Activity,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveBtn.callOnClick()
                }
            }
        }
    }

    private fun savePicture() {
        val currentBitmap = currentImage.drawable.toBitmap()
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            put(MediaStore.Images.Media.MIME_TYPE, "images/jpeg")
            put(MediaStore.Images.ImageColumns.WIDTH, currentBitmap.width)
            put(MediaStore.Images.ImageColumns.HEIGHT, currentBitmap.height)
        }
        val uri = contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues) ?: return
        println("output uri: $uri")
        contentResolver.openOutputStream(uri).use {
            println("Saving picture")
            currentBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
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


}