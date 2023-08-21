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
import android.widget.Button
import android.widget.ImageView
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


//camara y almacenamiento
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import android.view.View
import android.widget.Toast

class DatosUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDatosUsuarioBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val fireBase = Firebase.firestore
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var client: SettingsClient
    private var currentLocation: Location? = null
    private lateinit var locationSettingRequest: LocationSettingsRequest

    private val CAMERA_PERMISSION_REQUEST = 100
    private val STORAGE_PERMISSION_REQUEST = 101
    private val TAKE_PICTURE_REQUEST = 102
    private val PICK_IMAGE_REQUEST = 103

    private lateinit var usuarioImg: ImageView
    private lateinit var btnCambiarFoto: Button
    private var selectedImageUri: Uri? = null // Variable para almacenar la URI de la imagen seleccionada o capturada


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        binding = ActivityDatosUsuarioBinding.inflate(layoutInflater)
        client = LocationServices.getSettingsClient(this)
        setContentView(binding.root)

        // Inicialización de vistas
        usuarioImg = findViewById(R.id.usuarioImg)
        btnCambiarFoto = findViewById(R.id.btnCambiarFoto)

        // Configuración del listener para el botón "Cambiar Foto de Perfil"
        btnCambiarFoto.setOnClickListener {
            val options = arrayOf("Tomar Foto", "Seleccionar de la Galería")
            AlertDialog.Builder(this)
                .setTitle("Elige una opción")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> {
                            // Opción: Tomar Foto
                            if (ContextCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                abrirCamara()
                            } else {
                                ActivityCompat.requestPermissions(
                                    this,
                                    arrayOf(Manifest.permission.CAMERA),
                                    CAMERA_PERMISSION_REQUEST
                                )
                            }
                        }
                        1 -> {
                            // Opción: Seleccionar de la Galería
                            if (ContextCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                abrirGaleria()
                            } else {
                                ActivityCompat.requestPermissions(
                                    this,
                                    arrayOf(
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    ),
                                    STORAGE_PERMISSION_REQUEST
                                )
                            }
                        }
                    }
                }
                .show()
        }
    }

    private fun abrirCamara() {
        // Crear un intent para abrir la cámara y capturar una imagen
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, TAKE_PICTURE_REQUEST)
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // Manejo de la respuesta a las solicitudes de permisos
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST -> {
                // Verificar si el permiso de cámara fue concedido
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Si el permiso fue concedido, abrir la cámara
                    abrirCamara()
                }
            }
            STORAGE_PERMISSION_REQUEST -> {
                // Verificar si el permiso de almacenamiento fue concedido
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Si el permiso fue concedido, abrir la galería
                    abrirGaleria()
                }
            }
        }
    }

    // Manejo de la respuesta a las actividades de cámara y galería
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                TAKE_PICTURE_REQUEST -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    usuarioImg.setImageBitmap(imageBitmap)
                }
                PICK_IMAGE_REQUEST -> {
                    val imageUri: Uri = data?.data ?: return
                    usuarioImg.setImageURI(imageUri)
                }
            }
        }
    }

    private fun showUserData(item: Usuario) {
        binding.usuarioNombre.text = item.nombre
        binding.usuarioApellido.text = item.apellido
        binding.usuarFechaN.text = item.fechaNacimiento
        binding.usuarioNick.text = item.usuario
        binding.usuarioEmail.text = item.email
        binding.usuarioTelefono.text = item.numeroTelefono
    }

    private fun setupModifyButton(item: Usuario) {
        val user = Usuario(
            1,
            item.nombre,
            item.apellido,
            item.fechaNacimiento,
            item.usuario,
            item.email,
            "**",
            item.numeroTelefono
        )
        binding.buttonCambiar.setOnClickListener {
            sendDatoUsuario(user)
        }
    }

    override fun onStart() {
        super.onStart()
        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, //tipo de localizacion
            2000
        )//intervalo de actualizacion
            // .setMaxUpdates(3) //cuantas veces vamos a solicitar la ubicacion
            .build()
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
        val user = intent.getParcelableExtra<Usuario>("usuario")
        getUsuario(user?.usuario.toString()) { item ->
            if (item != null) {
                showUserData(item)
                setupModifyButton(item)
            } else {
                showSnackbar("Usuario no encontrado")
            }
        }
        initClass()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun getUsuario(usuario: String, callback: (Usuario?) -> Unit) {
        val TAG = "Memoraid datos firebase"
        val usersCollection = fireBase.collection("usuarios")
        usersCollection
            .whereEqualTo("usuario", usuario)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val document = result.documents[0]
                    val data = document.data
                    val user = Usuario(
                        1,
                        data?.get("nombre") as? String ?: "",
                        data?.get("apellido") as? String ?: "",
                        data?.get("fechaNacimiento") as? String ?: "",
                        data?.get("usuario") as? String ?: "",
                        data?.get("email") as? String ?: "",
                        data?.get("contrasena") as? String ?: "",
                        data?.get("numeroTelefono") as? String ?: ""
                    )
                    Log.d(TAG, user.toString())
                    Log.d(TAG, "${document.id} => ${document.data}")
                    callback(user)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error al obtener el dato", exception)
                callback(null)
            }
    }

    private fun sendDatoUsuario(item: Usuario) {
        val intent = Intent(this, ModificarDatosUsuarioActivity::class.java)
        intent.putExtra("usuario", item)
        startActivity(intent)
    }

    @SuppressLint("MissingPermission")
    private fun initClass() {
        binding.botonLocalizacion.setOnClickListener {
            locationContract.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

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
}