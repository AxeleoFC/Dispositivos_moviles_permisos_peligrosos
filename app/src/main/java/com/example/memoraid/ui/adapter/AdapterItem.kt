package com.example.memoraid.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memoraid.R
import com.example.memoraid.data.entities.DatosTienda
import com.example.memoraid.data.entities.Evento
import com.example.memoraid.databinding.ItemTiendaBinding
import com.squareup.picasso.Picasso

class AdapterItem(private var items: List<DatosTienda>) :
    RecyclerView.Adapter<AdapterItem.TiendaViewHolder>() {

    // Trabaja con la vista del método de abajo
    class TiendaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemTiendaBinding = ItemTiendaBinding.bind(view)

        fun render(item: DatosTienda) {
            binding.textViewNombreLocal.text = item.nombreLocal
            binding.textViewTipoLocal.text = item.tipoLocal
            binding.textViewUbicacionLocal.text = item.ubicacionLocal
            binding.textViewHorarioApertura.text = item.horarioApertura
            binding.textViewHorarioCierre.text = item.horarioCierre

            Picasso.get().load(item.imagenTienda).into(binding.imageViewLocal)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TiendaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        // Crea una vista
        return TiendaViewHolder(
            inflater.inflate(
                R.layout.item_tienda,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: TiendaViewHolder, position: Int) {
        val item = items[position]
        holder.render(item)
        // No hay evento de click, ya que hemos eliminado el setOnClickListener
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<DatosTienda>) {
        items = newItems
        notifyDataSetChanged()
    }
}