package com.example.memoraid.data.entities.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuariosDB(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val apellido: String,
    val fechaNacimiento: String,
    val usuario: String,
    val email: String,
    val contrasena: String,
    val numeroTelefono: String
)
