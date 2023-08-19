package com.example.memoraid.logic

import android.content.Context
import com.example.memoraid.data.connections.UserConnectionDB
import com.example.memoraid.data.dao.EventosDAO
import com.example.memoraid.data.entities.database.EventoDB

class EventosLogic {

    private lateinit var eventoDao: EventosDAO

    suspend fun getNotificaciones(context: Context): ArrayList<EventoDB> {
        eventoDao = UserConnectionDB.getDatabase(context).eventoDao()
        var list=eventoDao.getAllEventos()
        return list as ArrayList<EventoDB>
    }
}