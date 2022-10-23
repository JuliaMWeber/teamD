package com.example.gamecollection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
/*
class Adapter: BaseAdapter {
    var nameList = ArrayList<Item>()
    var context: Context? = null

    constructor(nameList: ArrayList<Item>, context: Context?) : super(){
        this.nameList = nameList
        this.context = context
    }

    override fun getCount(): Int {
        return nameList.size
    }

    override fun getItem(p0: Int): Any {
        return nameList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var nameGridList=this.nameList[p0]
        var inflator= context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
        as LayoutInflater

        var nameView=inflator.inflate(R.layout.item,null)
        nameView.image.setImageResource(nameGridList.image!!)
        nameView.textView.text= nameGridList.name!!
        return nameView
    }

}*/