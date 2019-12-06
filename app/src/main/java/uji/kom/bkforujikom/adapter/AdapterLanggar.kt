package uji.kom.bkforujikom.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import uji.kom.bkforujikom.R
import uji.kom.bkforujikom.model.Langgar

class AdapterLanggar (private val ctx: Context, imageModelArrayListMain: ArrayList<Langgar>) :
    RecyclerView.Adapter<AdapterLanggar.MyViewHolder>() {

    companion object {
        lateinit var imageModelArrayList: ArrayList<Langgar>
    }

    private val inflater: LayoutInflater
    // var imageModelArrayList: ArrayList<ReModel>

    init {

        inflater = LayoutInflater.from(ctx)
        imageModelArrayList = imageModelArrayListMain
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = inflater.inflate(R.layout.langgar_item, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.checkBox.isChecked = imageModelArrayList[position].getSelecteds()
        holder.tvAnimal.text = imageModelArrayList[position].getLangggar()

        // holder.checkBox.setTag(R.integer.btnplusview, convertView);
        holder.checkBox.tag = position

        holder.checkBox.setOnClickListener {
            val pos = holder.checkBox.tag as Int

            if (imageModelArrayList[pos].getSelecteds()) {
                imageModelArrayList[pos].setSelecteds(false)
            } else {
                imageModelArrayList[pos].setSelecteds(true)
            }
        }


    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var checkBox: CheckBox
        val tvAnimal: TextView

        init {

            checkBox = itemView.findViewById(R.id.cb) as CheckBox
            tvAnimal = itemView.findViewById(R.id.animal) as TextView
        }

    }

}