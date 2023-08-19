package com.example.memoraid.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.memoraid.data.entities.database.UsuariosDB

@Dao
interface UsuariosDAO {

    @Query("SELECT * FROM usuarios WHERE usuario = :userName AND contrasena = :password")
    fun getOneUser(userName: String, password: String): UsuariosDB?

    @Insert
    fun insertUser(ch: UsuariosDB)

}