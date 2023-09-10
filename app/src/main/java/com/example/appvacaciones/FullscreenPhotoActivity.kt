package com.example.appvacaciones

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class FullscreenPhotoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establece el diseño XML asociado con esta actividad.
        setContentView(R.layout.activity_fullscreen_photo)
        // Intenta recuperar el array de bytes extraído de la Intención que inició esta actividad.
        val byteArray = intent.getByteArrayExtra("photo")
        // Convierte el array de bytes en un bitmap. Si el array es nulo, el bitmap también será nulo.
        val bitmap = byteArray?.let { BitmapFactory.decodeByteArray(byteArray, 0, it.size) }
        // Encuentra el ImageView en el diseño usando su ID.
        val imageView: ImageView = findViewById(R.id.fullscreenImageView)
        // Establece el bitmap recuperado como imagen del ImageView.
        imageView.setImageBitmap(bitmap)
    }
}
