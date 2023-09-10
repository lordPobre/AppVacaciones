package com.example.appvacaciones

import android.Manifest
import android.content.pm.PackageManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream
import java.io.File


class MainActivity : AppCompatActivity() {
    // Constante para solicitar la captura de imagen.
    private val REQUEST_IMAGE_CAPTURE = 1
    // Lista que almacena los bitmaps de las fotos capturadas.
    private val photosList = mutableListOf<Bitmap>()
    // Adapter personalizado para mostrar las fotos en un RecyclerView.
    private lateinit var photosAdapter: PhotoAdapter
    // Adapter personalizado para mostrar las fotos en un RecyclerView.
    private lateinit var imageCapture: ImageCapture
    // Adapter personalizado para mostrar las fotos en un RecyclerView.
    private val PERMISSION_REQUEST_CODE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vacation)

        // Solicita los permisos necesarios para la app.
        requestPermissions()

        // Instancia el PhotoAdapter y asigna un click listener.
        // Cuando se hace clic en una imagen, se inicia una nueva actividad para mostrar la imagen en pantalla completa.
        photosAdapter = PhotoAdapter(photosList) { bitmap ->
            // Aquí puedes iniciar una nueva actividad o un fragmento para mostrar la imagen en pantalla completa
            val intent = Intent(this, FullscreenPhotoActivity::class.java)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
            val byteArray = stream.toByteArray()
            intent.putExtra("photo", byteArray)
            startActivity(intent)
        }
        val imageView = findViewById<ImageView>(R.id.fullscreenImageView)
        val recyclerView = findViewById<RecyclerView>(R.id.photosRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = photosAdapter

        // Establece un listener para el botón que inicia la actividad de Mapa.
        val btnShowOnMap: Button = findViewById(R.id.showMapsBtn)
        btnShowOnMap.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("latitude", 0.0)
            intent.putExtra("longitude", 0.0)
            startActivity(intent)
        }
        // Establece un listener para el botón que inicia la captura de fotos.
        val addButton: Button = findViewById(R.id.addPhotoBtn)
        addButton.setOnClickListener { dispatchTakePictureIntent() }

        // inicializa CameraX
        startCamera()
    }

    // Función para inicializar CameraX.
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture)
            } catch (exc: Exception) {
                Log.e("CameraX", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    // Función que se encarga de capturar una imagen con CameraX.
    private fun dispatchTakePictureIntent() {
        val file = File(externalMediaDirs.first(), "${System.currentTimeMillis()}.jpg")

        imageCapture.takePicture(
            ImageCapture.OutputFileOptions.Builder(file).build(),
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val msg = "Imagen guardada en: ${file.absolutePath}"
                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    photosList.add(bitmap)
                    photosAdapter.notifyItemInserted(photosList.size - 1)
                }

                override fun onError(exc: ImageCaptureException) {
                    Log.e("CameraX", "Photo capture failed: ${exc.message}", exc)
                }
            }
        )
    }

    // Función que solicita permisos necesarios para la app.
    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                Snackbar.make(findViewById(android.R.id.content), "Necesitamos estos permisos para guardar imágenes, acceder a la cámara y obtener tu ubicación.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("ENTENDIDO") {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
                    }
                    .show()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
            }
        }
    }

    // Función callback que maneja la respuesta del usuario a la solicitud de permisos.
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Aquí puedes proceder con la operación que requiere el permiso.
                } else {
                    // Permiso denegado. Puedes deshabilitar la funcionalidad que depende de este permiso.
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}



