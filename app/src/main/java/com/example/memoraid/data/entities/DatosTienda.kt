package com.example.memoraid.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DatosTienda(
    val id: Int,
    val nombreLocal: String,
    val tipoLocal: String,
    val ubicacionLocal: String,
    val horarioApertura: String,
    val horarioCierre: String,
    val imagenTienda: String
): Parcelable
