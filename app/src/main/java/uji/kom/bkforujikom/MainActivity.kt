package uji.kom.bkforujikom

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import uji.kom.bkforujikom.adapter.AdapterMainPelanggaran
import uji.kom.bkforujikom.model.Pelanggaran

class MainActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null
    private var hud: KProgressHUD? = null

    private var adapter: AdapterMainPelanggaran? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title_header.text = "List Pelanggaran"

        auth = FirebaseAuth.getInstance()
        val user = auth!!.currentUser

        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        img_menu.visibility = View.VISIBLE
        img_menu.onClick {
            var popMenu = PopupMenu(this@MainActivity, title_header)
            popMenu.setOnMenuItemClickListener {
                if (it.itemId == R.id.menu_logout)
                    alertLogout(auth!!)
                return@setOnMenuItemClickListener true
            }
            popMenu.inflate(R.menu.main_menu)
            popMenu.show()
        }

        getData()
        fabAdd.onClick {
            startActivity(Intent(applicationContext, AddPelanggaranActivity::class.java))
        }
    }

    private fun alertLogout(auth: FirebaseAuth){
        alert("Log Out"){
            yesButton {
                auth.signOut()
                if (auth.currentUser == null) {
                    startActivity<LoginActivity>()
                    finish()
                }
            }
            noButton {}
        }.show()
    }

    private fun getData() {
        showLoading("Generating Data")
        var listLanggar: ArrayList<Pelanggaran>? = ArrayList()
        FirebaseDatabase.getInstance().getReference("pelanggaran")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(data: DataSnapshot) {
                    hud!!.dismiss()
                    for (ds in data.children) {
                        var langgar= ds.getValue(Pelanggaran::class.java)
                        listLanggar!!.add(langgar!!)
                    }

                    if (listLanggar!!.isEmpty())
                        txtNothing.visibility = View.VISIBLE
                    else
                        txtNothing.visibility = View.GONE

                    adapter = AdapterMainPelanggaran(this@MainActivity, listLanggar)
                    rv_main.adapter = adapter
                    rv_main.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
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
