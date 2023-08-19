package com.example.memoraid.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.memoraid.data.entities.database.EventoDB

@Dao
interface EventosDAO {

    @Query("SELECT * FROM eventos ORDER BY fecha ASC")
    fun getAllEventos(): List<EventoDB>?

    @Insert
    fun insertEvento(ch: EventoDB)

}