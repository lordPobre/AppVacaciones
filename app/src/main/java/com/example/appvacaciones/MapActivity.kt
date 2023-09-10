package com.example.appvacaciones

import android.app.Activity
import android.os.Bundle
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  Crear un objeto mapView y establecerlo como contenido de la actividad.
        val mapView = MapView(this)
        setContentView(mapView)
        // Obtener las coordenadas (latitud y longitud) enviadas a través del intent.
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        // Crear un punto geográfico con esas coordenadas.
        val startPoint = GeoPoint(latitude, longitude)
        // Configurar el controlador del mapa para centrarse en ese punto y establecer un nivel de zoom.
        val mapController = mapView.controller
        mapController.setCenter(startPoint)
        mapController.zoomTo(15)

        // Agregar un marcador en el punto geográfico en el mapa.
        val marker = Marker(mapView)
        marker.position = startPoint
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        mapView.overlays.add(marker)
    }
}
