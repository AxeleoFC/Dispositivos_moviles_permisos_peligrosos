package com.example.memoraid.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memoraid.data.entities.DatosTienda
import com.example.memoraid.data.entities.Evento
import com.example.memoraid.databinding.ActivityRlocalBinding
import com.example.memoraid.ui.adapter.AdapterItemTipo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.Console
import kotlin.math.log

class RLocalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRlocalBinding
    private var fireBase = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Esto arreglar
        val item = intent.getParcelableExtra<Evento>("tipoLocal")
        Log.d("Locales", "${item?.tipo}")
        binding = ActivityRlocalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(item != null){
            getData(item?.tipo.toString())
        }
    }

    private fun getData(tipo:String ) {
        val adapter = AdapterItemTipo(
            ArrayList()
        ) { sendDatoItem(it)}
        val rvDatos = binding.rvItems
        rvDatos.adapter = adapter
        rvDatos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // Llamar a getEventos y actualizar el adaptador con los datos obtenidos
        getLocalPorTipo(tipo) { locales ->
            adapter.updateData(locales)
        }
    }


    private fun getLocalPorTipo(tipoLocal: String, callback: (ArrayList<DatosTienda>) -> Unit) {
        val TAG = "Memoraid Local"
        val localList = ArrayList<DatosTienda>()
        fireBase.collection("tiendas")
            .whereEqualTo("tipoLocal", tipoLocal) // Aplicar el filtro por tipo de evento
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val data = document.data
                    localList.add(
                        DatosTienda(
                            1,
                            data["nombreLocal"] as String,
                            data["tipoLocal"] as String,
                            data["ubicacionLocal"] as String,
                            data["horarioApertura"] as String,
                            data["horarioCierre"] as String,
                            data["imagenTienda"] as String
                        )
                    )
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
                callback(localList)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
    private fun sendDatoItem(item: DatosTienda):Unit {
        val i = Intent(this, RLocalActivity::class.java)
        i.putExtra("tipoLocal",item)
        startActivity(i)
    }
}