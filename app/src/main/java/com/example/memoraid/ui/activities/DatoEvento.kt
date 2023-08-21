package com.example.memoraid.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memoraid.data.entities.DatosTienda
import com.example.memoraid.data.entities.Evento
import com.example.memoraid.data.entities.database.EventoDB
import com.example.memoraid.databinding.ActivityDatoEventoBinding
import com.example.memoraid.ui.adapter.AdapterItem
import com.example.memoraid.ui.adapter.AdapterItemTipo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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
    }


    private fun sendDatoItem(item: Evento):Unit {
        val i = Intent(this, RLocalActivity::class.java)
        i.putExtra("tipoLocal",item)
        startActivity(i)
    }
}