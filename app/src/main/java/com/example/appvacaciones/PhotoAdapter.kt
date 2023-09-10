package com.example.appvacaciones

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class PhotoAdapter(private val photos: List<Bitmap>, private val clickListener: (Bitmap) -> Unit)
    : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {
    // ViewHolder para representar un elemento de la lista.
    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.photoImageView)
        // Bloque inicializador para el ViewHolder.
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    clickListener(photos[position])
                }
            }
        }
    }
    // Método que infla el diseño del elemento y crea y devuelve el ViewHolder.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(view)
    }
    // Método que vincula los datos (en este caso, una imagen) al ViewHolder.
    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.imageView.setImageBitmap(photos[position])
    }
    // Devuelve el número total de elementos (imágenes) que tiene el adaptador.
    override fun getItemCount(): Int = photos.size
}

