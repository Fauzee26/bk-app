package uji.kom.bkforujikom.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import uji.kom.bkforujikom.model.Siswa

class SpinnerAdapterSiswa(context: Context, res: Int, private var data: ArrayList<Siswa>):
    ArrayAdapter<Siswa>(context, res, data){

    override fun getItem(position: Int): Siswa? {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getView(position, convertView, parent) as TextView
        label.text = data[position].nama

        return label
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getView(position, convertView, parent) as TextView
        label.text = data[position].nama

        return label
    }
}