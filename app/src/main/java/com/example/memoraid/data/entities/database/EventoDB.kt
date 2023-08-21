package com.example.memoraid.data.entities.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

@Parcelize
@Entity(tableName = "eventos")
data class EventoDB(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val usuarioID:String,
    val tipo: String,
    val fecha: Long,
    val fechaRecordar: Long,
    val presupuesto: Double
) : Parcelable{
    fun getFormattedFecha(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = Date(fecha)
        return sdf.format(date)
    }

    fun getFormattedFechaRecordar(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateRecordar = Date(fechaRecordar)
        return sdf.format(dateRecordar)
    }
}

