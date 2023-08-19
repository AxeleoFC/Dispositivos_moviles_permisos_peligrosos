package com.example.memoraid.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memoraid.R
import com.example.memoraid.data.entities.Evento
import com.example.memoraid.data.entities.database.EventoDB
import com.example.memoraid.databinding.EventoProximoBinding

class EventosAdapter(private var items: List<Evento>, private var fnClick:(Evento)->Unit):
    RecyclerView.Adapter<EventosAdapter.EventosViewHolder>() {

    class EventosViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding: EventoProximoBinding = EventoProximoBinding.bind(view)

        //    solo modificamos el render
        fun render(
            item: Evento, fnClick:(Evento)->Unit){
            binding.eventoNum.text = item.tipo
            binding.fechaEvento.text = item.fecha.toString()
            itemView.setOnClickListener{
                fnClick(item)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): EventosAdapter.EventosViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return EventosViewHolder(
            inflater.inflate(
                R.layout.evento_proximo,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: EventosViewHolder, position: Int) {
        holder.render(items[position], fnClick)
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<Evento>) {
        items = newItems
        notifyDataSetChanged()
    }
}