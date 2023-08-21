package com.example.memoraid.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memoraid.data.entities.Evento
import com.example.memoraid.data.entities.Usuario
import com.example.memoraid.databinding.ActivityDatosUsuarioBinding

class AdapterUsuario (private var items: Usuario, private var fnClick:(Usuario)->Unit):
     RecyclerView.Adapter<AdapterUsuario.UsuarioViewHolder>() {

    class UsuarioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ActivityDatosUsuarioBinding= ActivityDatosUsuarioBinding.bind(view)
        //    solo modificamos el render
        fun render(
            item: Usuario, fnClick:(Usuario)->Unit){

            binding.usuarioNombre.text=item.nombre
            binding.usuarioApellido.text=item.apellido
            binding.usuarFechaN.text=item.fechaNacimiento
            binding.usuarioNick.text=item.usuario
            binding.etiquetaEmail.text=item.email
            binding.usuarioTelefono.text=item.numeroTelefono

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterUsuario.UsuarioViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: AdapterUsuario.UsuarioViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }


}