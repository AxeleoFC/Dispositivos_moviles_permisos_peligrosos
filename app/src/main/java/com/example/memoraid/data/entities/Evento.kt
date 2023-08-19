package com.example.memoraid.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Evento(
    val id: Int,
    val tipo: String,
    val fecha: String,
    val fechaRecordar: String,
    val presupuesto: Double
): Parcelable