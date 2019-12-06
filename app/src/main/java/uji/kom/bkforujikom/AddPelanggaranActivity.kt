package uji.kom.bkforujikom

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_add_pelanggaran.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import uji.kom.bkforujikom.adapter.AdapterLanggar
import uji.kom.bkforujikom.adapter.SpinnerAdapterKelas
import uji.kom.bkforujikom.adapter.SpinnerAdapterSiswa
import uji.kom.bkforujikom.model.Kelas
import uji.kom.bkforujikom.model.Langgar
import uji.kom.bkforujikom.model.Pelanggaran
import uji.kom.bkforujikom.model.Siswa
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddPelanggaranActivity : AppCompatActivity() {

    private var adapterKelas: SpinnerAdapterKelas? = null
    private var adapterSiswa: SpinnerAdapterSiswa? = null
    private var adapterLanggar: AdapterLanggar? = null

    private var hud: KProgressHUD? = null

    private var posKelas: Int? = null
    private var selectedKelas: String? = null
    private var selectedSiswa: String? = null

    private var langgarItems: ArrayList<Langgar> = ArrayList()
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pelanggaran)

        getKelas()
//        getLanggarItem()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd MMMM yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                edtTglPelanggaran!!.text = sdf.format(cal.time)
            }

        edtTglPelanggaran.onClick {
            val dialog = DatePickerDialog(this@AddPelanggaranActivity,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH))

            dialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
            dialog.show()
        }

        btnSubmit.onClick {
            if (TextUtils.isEmpty(edtTglPelanggaran.text)) {
                toast("Fill the date")
                return@onClick
            }

            var isThere = false
            for (i in 0 until AdapterLanggar.imageModelArrayList.size){
                if (AdapterLanggar.imageModelArrayList[i].getSelecteds()){
                    isThere = true
                }
            }
            if (isThere == false) {
                toast("Pilih salah satu pelanggaran siswa")
                return@onClick
            }

            showLoading("Submit Data")

            var listPelanggaran: String? = ""
            for (i in 0 until AdapterLanggar.imageModelArrayList.size){
                if (AdapterLanggar.imageModelArrayList[i].getSelecteds()){
                    listPelanggaran = AdapterLanggar.imageModelArrayList[i].getLangggar() + ", " + listPelanggaran
                }
            }
            val reference = FirebaseDatabase.getInstance().getReference("pelanggaran")
            val pelanggaranNew = Pelanggaran(selectedSiswa, cal.timeInMillis.toString(), edtWaliKelas.text.toString(),
                listPelanggaran, edtKeterangan.text.toString(), selectedKelas)

            reference.child(System.currentTimeMillis().toString()).setValue(pelanggaranNew).addOnSuccessListener {
                hud!!.dismiss()
                startActivity<MainActivity>()
                finish()
            }

        }
    }

    private fun getLanggarItem() {
        showLoading("Generating Data")
        var listLanggar: ArrayList<String>? = ArrayList()
        FirebaseDatabase.getInstance().getReference("list")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(data: DataSnapshot) {
                    hud!!.dismiss()
                    for (ds in data.children) {
                        var langgar= ds.getValue(String::class.java)
                        listLanggar!!.add(langgar!!)
                    }

                    langgarItems = getModel(false, listLanggar!!)
                    adapterLanggar = AdapterLanggar(this@AddPelanggaranActivity, langgarItems)
                    rv_pelanggaran.adapter = adapterLanggar
                    rv_pelanggaran.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                }

                override fun onCancelled(err: DatabaseError) {
                    hud!!.dismiss()
                    toast(err.message)
                }
            })
    }

    private fun getModel(isSelect: Boolean, langgarList: ArrayList<String>): ArrayList<Langgar> {
        val list = ArrayList<Langgar>()
        for (i in 0 until langgarList.size) {

            val model = Langgar()
            model.setSelecteds(isSelect)
            model.setLangggar(langgarList[i])
            list.add(model)
        }
        return list
    }
    private fun getSiswa(selectedKelas: String?) {
        showLoading("Generating Data")
        var listSiswa: ArrayList<Siswa>? = ArrayList()
        FirebaseDatabase.getInstance().getReference("siswa/$selectedKelas")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(data: DataSnapshot) {
                    hud!!.dismiss()
                    for (ds in data.children) {
                        var siswa= ds.getValue(Siswa::class.java)
                        var kelasNama = ds.child("nama").getValue(String::class.java)
                        var kelasWali = ds.child("asal").getValue(String::class.java)

                        val kelasList = Kelas(kelasNama, kelasWali)
                        listSiswa!!.add(siswa!!)
                    }

                    adapterSiswa = SpinnerAdapterSiswa(
                        applicationContext,
                        android.R.layout.simple_spinner_dropdown_item,
                        listSiswa!!
                    )
                    spinNamaSiswa.adapter = adapterSiswa
                    spinNamaSiswa.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                            val siswa = spinNamaSiswa.selectedItem as Siswa

                            selectedSiswa = siswa.nama
                            getLanggarItem()
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }
                    }
                }

                override fun onCancelled(err: DatabaseError) {
                    hud!!.dismiss()
                    toast(err.message)
                }
            })
    }

    private fun getKelas() {
        showLoading("Generating Data")
        var listKelas: ArrayList<Kelas>? = ArrayList()
        FirebaseDatabase.getInstance().getReference("kelas")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(data: DataSnapshot) {
                    hud!!.dismiss()
                    for (ds in data.children) {
                        var kelasNama = ds.child("nama").getValue(String::class.java)
                        var kelasWali = ds.child("wali").getValue(String::class.java)

                        val kelasList = Kelas(kelasNama, kelasWali)
                        listKelas!!.add(kelasList)
                    }

                    adapterKelas = SpinnerAdapterKelas(
                        this@AddPelanggaranActivity,
                        android.R.layout.simple_spinner_dropdown_item,
                        listKelas!!
                    )
                    spinPilihKelas.adapter = adapterKelas
                    spinPilihKelas.setSelection(0)
                    spinPilihKelas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                            val kelas = spinPilihKelas.selectedItem as Kelas

                            edtWaliKelas.text = kelas.wali

                            selectedKelas = kelas.nama
                            getSiswa(selectedKelas)

                            posKelas = spinPilihKelas.selectedItemPosition
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }
                    }
                }

                override fun onCancelled(err: DatabaseError) {
                    hud!!.dismiss()
                    toast(err.message)
                }
            })
    }

    private fun showLoading(label: String) {
        hud = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setDetailsLabel(label)
            .setCancellable(true)
            .setAnimationSpeed(1)
            .setDimAmount(0.5f)
        hud!!.show()
    }
}
