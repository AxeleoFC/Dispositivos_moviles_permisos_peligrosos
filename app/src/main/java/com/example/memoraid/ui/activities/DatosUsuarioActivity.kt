package com.example.memoraid.ui.activities

import android.annotation.SuppressLint

import android.content.Intent
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.memoraid.R
import com.example.memoraid.data.entities.Evento
import com.example.memoraid.data.entities.Usuario
import com.example.memoraid.data.entities.database.UsuariosDB
import com.example.memoraid.databinding.ActivityDatosUsuarioBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DatosUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDatosUsuarioBinding

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val fireBase = Firebase.firestore
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var client: SettingsClient
    private var currentLocation: Location? = null

    private lateinit var locationSettingRequest: LocationSettingsRequest

    @SuppressLint("MissingPermission")
    val locationContract = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        when (isGranted) {
            true -> {

                client.checkLocationSettings(locationSettingRequest).apply {
                    addOnSuccessListener {
                        val task = fusedLocationProviderClient.lastLocation
                        task.addOnSuccessListener { location ->
                            fusedLocationProviderClient.requestLocationUpdates(
                                locationRequest,
                                locationCallback,
                                Looper.getMainLooper()
                            )
                        }
                    }

                    addOnFailureListener() { ex ->
                        if (ex is ResolvableApiException) {
                            ex.startResolutionForResult(
                                this@DatosUsuarioActivity,
                                LocationSettingsStatusCodes.RESOLUTION_REQUIRED
                            )
                        }

                    }
                }

                val task = fusedLocationProviderClient.lastLocation






                task.addOnSuccessListener { location ->


                    //actualizar la localizacion
                    fusedLocationProviderClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper()
                    )


                }

                val alert = AlertDialog.Builder(
                    this
                )
                alert.apply {
                    //setTitle("Notificacion")
                    setMessage("Memoraid accedio a la ubicacion del dispositivo.")
                    setPositiveButton("OK") { dialog, id ->

                        val i = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                        startActivity(i)
//                        dialog.dismiss()
                    }

                    setCancelable(false)
                }.show()

//                task.addOnFailureListener { ex ->
//                    if (ex is ResolvableApiException) {
//                        ex.startResolutionForResult(
//                            this@MainActivity,
//                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED
//                        )
//                    }
//                }


            }

            shouldShowRequestPermissionRationale(
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                Snackbar.make(
                    binding.etiquetaEmail,
                    "Ayude con el permiso porfa",
                    Snackbar.LENGTH_LONG
                ).show()
            }

            false -> {
                Snackbar.make(
                    binding.etiquetaEmail,
                    "Denegado",
                    Snackbar.LENGTH_LONG
                ).show()
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        binding = ActivityDatosUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, //tipo de localizacion
            2000
        )//intervalo de actualizacion
            // .setMaxUpdates(3) //cuantas veces vamos a solicitar la ubicacion
            .build()

        client = LocationServices.getSettingsClient(this)
        locationSettingRequest =
            LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()

        //clase abstracta no se puede instanciar, se esta heredando a la variable los metodos que tiene
        locationCallback = object : LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                if (locationResult != null) {

                    locationResult.locations.forEach { location ->
                        currentLocation = location
                        Log.d("UCE", "Ubicacion: ${location.latitude}, ${location.longitude}")

                    }
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        initClass()

        val user = Usuario(
            1,
            binding.usuarioNombre.toString(),
            binding.usuarioApellido.toString(),
            binding.usuarFechaN.toString(),
            binding.usuarioNick.toString(),
            binding.etiquetaEmail.toString(),
            "**",
            binding.usuarioTelefono.toString()
        )
        binding.buttonCambiar.setOnClickListener {
            sendDatoUsuario(user)
        }

    }

    @SuppressLint("MissingPermission")
    private fun initClass() {





        binding.botonLocalizacion.setOnClickListener {
            locationContract.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)



        }

    }


    private fun getUsuario(callback: (UsuariosDB) -> Unit) {
        val TAG = "Memoraid"
        val eventosList = ArrayList<Evento>()
        fireBase.collection("usuario")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val data = document.data
                    val user = UsuariosDB(
                        1,
                        data["nombre"] as String,
                        data["apellido"] as String,
                        data["fechaNacimiento"] as String,
                        data["usuario"] as String,
                        data["email"] as String,
                        data["contrasena"] as String,
                        data["numeroTelefono"] as String
                    )
                    Log.d(TAG, "${document.id} => ${document.data}")
                    callback(user)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    fun sendDatoUsuario(item: Usuario): Unit {
        val i = Intent(this, ModificarDatosUsuarioActivity::class.java)
        i.putExtra("usuario", item)
        startActivity(i)

    }
}