package de.thm.mow2gamecollection.controller.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.sudoku.model.Game

class MyAdapter (private val context:Activity, private val arrayList:ArrayList<Game>) : ArrayAdapter<Game>(context,
    R.layout.list_item,arrayList){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //val inflater: LayoutInflater= LayoutInflater.from(context)
        val inflater = context.layoutInflater
        val view: View = inflater.inflate(R.layout.list_item,null)

        val imageView:ImageView =view.findViewById(R.id.profile_pic)
        val gamesname: TextView = view.findViewById(R.id.gamesName)

        imageView.setImageResource(arrayList[position].imageId)
        gamesname.text = arrayList[position].name
        return view

        //return super.getView(position, convertView, parent)
    }
}