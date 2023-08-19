package com.example.memoraid.ui.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class DatePikerFragment(val listener:(day:Int, month:Int, year:Int)->Unit): DialogFragment(),
    DatePickerDialog.OnDateSetListener {

    //cuando el usuario eliga una fecha va correr esta funcion
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayofMonth: Int) {
        //llama al codigo de la clase Menu principal
        listener(dayofMonth,month,year)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //Clase q nos indica q fecha es hoy

        val c:Calendar= Calendar.getInstance()
        val day=c.get(Calendar.DAY_OF_MONTH)
        val month=c.get(Calendar.MONTH)
        val year=c.get(Calendar.YEAR)


        val picker=DatePickerDialog(activity as Context,this,year,month,day)
        picker.datePicker.minDate=c.timeInMillis
        return picker

    }
}