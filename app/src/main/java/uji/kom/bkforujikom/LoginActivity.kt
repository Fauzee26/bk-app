package uji.kom.bkforujikom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import uji.kom.bkforujikom.model.User
import uji.kom.bkforujikom.util.Preference

class LoginActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null
    private var hud: KProgressHUD? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        txtRegister.onClick {
            startActivity<RegisterActivity>()
            finish()
        }

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            if (TextUtils.isEmpty(email)) {
                toast("Email Field Can't be null")
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                toast("Password Field can't be null")
                return@setOnClickListener
            }

            login(auth!!, email, password)

        }
    }

    private fun login(auth: FirebaseAuth, email: String, password: String) {
        showLoading()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                hud!!.dismiss()
                if (it.isSuccessful) {
                    FirebaseDatabase.getInstance().getReference("users")
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(data: DataSnapshot) {
                                for (item in data.children){
                                    val user = item.getValue(User::class.java)
                                    if (user!!.uid.equals(auth.currentUser!!.uid)){
                                        val pref = Preference(applicationContext)
                                        pref.setUid(user.uid!!)
                                        pref.setEmail(user.email!!)
                                        pref.setFullname(user.username!!)

                                        startActivity(Intent(applicationContext, MainActivity::class.java))
                                        finish()
                                    }
                                }
                            }
                            override fun onCancelled(err: DatabaseError) {
                                toast(err.message)
                            }

                        })
                } else {
                    toast("Login Failed")
                }
            }

            .addOnFailureListener(this) {
                hud!!.dismiss()
                toast(it.message!!)
            }
    }


    private fun showLoading() {
        hud = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
        hud!!.show()
    }
}
