package com.example.memoraid.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.memoraid.R

class ColoredSpinnerAdapter(
    context: Context,
    private val items: List<String>,
    private val textColors: List<Int>
) : ArrayAdapter<String>(context, R.layout.simple_spinner_layout, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent) as TextView
        view.text = items[position]
        view.setTextColor(textColors[position])
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent) as TextView
        view.text = items[position]
        view.setTextColor(textColors[position])
        return view
    }
}