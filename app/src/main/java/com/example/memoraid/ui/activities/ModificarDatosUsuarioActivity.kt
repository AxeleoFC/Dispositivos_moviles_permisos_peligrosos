package com.example.memoraid.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.memoraid.R
import com.example.memoraid.data.entities.Evento
import com.example.memoraid.data.entities.Usuario
import com.example.memoraid.databinding.ActivityModificarDatosUsuarioBinding

class ModificarDatosUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModificarDatosUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityModificarDatosUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        val item = intent.getParcelableExtra<Usuario>("usuario")
        if(item != null){
            /*binding.usuarioNombre.text = item.nombre
            binding.usuarioApellido.text = item.apellido.toString()
            binding.usuarFechaN.text = item.fechaNacimiento.toString()
            binding.usuarioNick.text = item.usuario.toString()


            binding.buttonGuardar.setOnClickListener {
                //sendDatoItem(a)
            }*/
        }
    }
}