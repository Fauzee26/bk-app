package uji.kom.bkforujikom.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import uji.kom.bkforujikom.DetailActivity
import uji.kom.bkforujikom.R
import uji.kom.bkforujikom.model.Pelanggaran
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AdapterMainPelanggaran (private val ctx: Context, val pelanggaranData: ArrayList<Pelanggaran>) :
    RecyclerView.Adapter<AdapterMainPelanggaran.MyViewHolder>() {

    private val inflater: LayoutInflater

    init {

        inflater = LayoutInflater.from(ctx)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = inflater.inflate(R.layout.main_item, parent, false)

        return MyViewHolder(view)
    }
    override fun getItemCount(): Int {
        return pelanggaranData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        holder.etPelanggarTgl.text = dateFormat.format(Date(pelanggaranData[position].tgl!!.toLong()))
        holder.etPelanggarNama.text = pelanggaranData[position].nama
        holder.etPelanggarWali.text = pelanggaranData[position].wali
        holder.etPelanggarList.text = pelanggaranData[position].pelanggaran
        holder.etPelanggarKelas.text = pelanggaranData[position].kelas

        holder.itemView.onClick {
            ctx.startActivity<DetailActivity>("detail" to pelanggaranData[position])
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val etPelanggarTgl: TextView
        val etPelanggarNama: TextView
        val etPelanggarWali: TextView
        val etPelanggarList: TextView
        val etPelanggarKelas: TextView

        init {
            etPelanggarTgl = itemView.findViewById(R.id.etPelanggarTgl) as TextView
            etPelanggarNama = itemView.findViewById(R.id.etPelanggarNama) as TextView
            etPelanggarWali = itemView.findViewById(R.id.etPelanggarWali) as TextView
            etPelanggarList = itemView.findViewById(R.id.etPelanggarList) as TextView
            etPelanggarKelas = itemView.findViewById(R.id.etPelanggarKelas) as TextView
        }

    }
}