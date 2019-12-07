package uji.kom.bkforujikom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import uji.kom.bkforujikom.model.Pelanggaran
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val pelanggaran = intent.getParcelableExtra<Pelanggaran>("detail")

        txtDetailKelas.text = pelanggaran.kelas
        txtDetailName.text = pelanggaran.nama
        txtDetailWali.text = pelanggaran.wali
        txtDetailKet.text = pelanggaran.ket
        txtDetailPelapor.text = pelanggaran.pelapor

        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        txtDetailTgl.text = dateFormat.format(Date(pelanggaran.tgl!!.toLong()))

        val listPel = pelanggaran.pelanggaran!!.replace(", ", "\n")
        txtDetailPelanggaran.text = listPel
    }
}
