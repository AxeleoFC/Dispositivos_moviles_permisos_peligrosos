package com.example.memoraid.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.memoraid.data.entities.Evento
import com.example.memoraid.databinding.ActivityDatoEventoBinding

class DatoEvento : AppCompatActivity() {

    private lateinit var binding: ActivityDatoEventoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDatoEventoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    override fun onStart() {
        super.onStart()
        val item = intent.getParcelableExtra<Evento>("evento")
        if(item != null){
            binding.datoEvento.text=item.tipo.toString()
            binding.datoFecha.text=item.fecha.toString()
            binding.datoFechaRecordatorio.text=item.fechaRecordar.toString()
            binding.datoPresupuesto.text=item.presupuesto.toString()

            binding.btnBuscarLoca.setOnClickListener {
                sendDatoItem(Evento(0
                    , ""
                    , binding.datoEvento.text.toString()
                    ,""
                    ,""
                    ,0.0))
            }
        }
        binding.btnMensaje.setOnClickListener {
            val intent= Intent(this,MensajeProgActivity::class.java)
            startActivity(intent)
        }
    }


    private fun sendDatoItem(item: Evento):Unit {
        val i = Intent(this, RLocalActivity::class.java)
        i.putExtra("tipoLocal",item)
        startActivity(i)
    }
}