package uji.kom.bkforujikom.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import uji.kom.bkforujikom.model.Kelas

class SpinnerAdapterKelas(private var context: Activity, private var res: Int, private var data: ArrayList<Kelas>):
    ArrayAdapter<Kelas>(context, res, data){

    override fun getItem(position: Int): Kelas? {
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