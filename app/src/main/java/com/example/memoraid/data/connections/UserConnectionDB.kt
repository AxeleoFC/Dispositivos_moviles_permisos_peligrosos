package com.example.memoraid.data.connections

import android.content.Context
import androidx.room.Database
import androidx.room.Room

import androidx.room.RoomDatabase
import com.example.memoraid.data.dao.EventosDAO
import com.example.memoraid.data.dao.UsuariosDAO
import com.example.memoraid.data.entities.database.EventoDB
import com.example.memoraid.data.entities.database.UsuariosDB

@Database(
    entities = [UsuariosDB::class , EventoDB::class],
    version = 2
)

abstract class UserConnectionDB : RoomDatabase() {
    abstract fun usuarioDao(): UsuariosDAO
    abstract fun eventoDao(): EventosDAO

    companion object {
        private var instance: UserConnectionDB? = null

        fun getDatabase(context: Context): UserConnectionDB {
            return instance ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context.applicationContext,
                    UserConnectionDB::class.java,
                    "app_database"
                ).build()
                instance = database
                database
            }
        }
    }
}