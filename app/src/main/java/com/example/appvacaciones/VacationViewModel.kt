package com.example.appvacaciones

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class VacationViewModel : ViewModel() {
    // Lista de imágenes
    val images = mutableStateListOf<Bitmap>()

    // Información del lugar
    val placeName = mutableStateOf("")

    // Posición geográfica
    val latitude = mutableStateOf(0.0)
    val longitude = mutableStateOf(0.0)

    // Agregar una imagen a la lista
    fun addImage(image: Bitmap) {
        images.add(image)
    }

    // Configurar el nombre del lugar y la posición
    fun setPlaceInfo(name: String, lat: Double, lon: Double) {
        placeName.value = name
        latitude.value = lat
        longitude.value = lon
    }
}
