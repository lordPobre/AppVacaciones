package com.example.appvacaciones

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class VacationActivity : AppCompatActivity() {
    // Declaración de variables de instancia.
    private lateinit var placeName: EditText
    private lateinit var imageContainer: LinearLayout
    private val REQUEST_IMAGE_PICK = 1001  // Constante para identificar la selección de imagen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vacation)
    // Declaración de variables de instancia.
        placeName = findViewById(R.id.placeName)
        imageContainer = findViewById(R.id.imageContainer)
        val addPhotoBtn = findViewById<Button>(R.id.addPhotoBtn)
        val showMapBtn = findViewById<Button>(R.id.showMapsBtn)

        addPhotoBtn.setOnClickListener {
            // Lanzar la galería para seleccionar una imagen
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }

        showMapBtn.setOnClickListener {
            // Lanzar actividad del mapa
        }
    }

    private fun addImageToLayout(bitmap: Bitmap) {
        val imageView = ImageView(this)
        imageView.setImageBitmap(bitmap)
        imageView.layoutParams = LinearLayout.LayoutParams(100, 100)
        imageView.setOnClickListener {
            // Mostrar en pantalla completa
        }
        imageContainer.addView(imageView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImageUri)
            addImageToLayout(bitmap)
        }
    }
}
