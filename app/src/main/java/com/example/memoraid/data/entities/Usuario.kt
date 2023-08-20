package com.example.memoraid.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Usuario(
    val id:Int,
    val nombre: String,
    val apellido: String,
    val fechaNacimiento: String,
    val usuario: String,
    val email: String,
    val contrasena: String,
    val numeroTelefono: String): Parcelable
