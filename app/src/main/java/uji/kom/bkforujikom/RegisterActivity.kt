package uji.kom.bkforujikom

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.toast
import uji.kom.bkforujikom.model.User

class RegisterActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null
    private var hud: KProgressHUD? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        txtLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnRegister.setOnClickListener {
            val fullname = edtFullname.text.toString()
            val email = edtEmail.text.toString()
            val pass = edtPassword.text.toString()
            val confirmPass = edtPasswordConfirmation.text.toString()


            if (TextUtils.isEmpty(fullname)) {
                toast("fullname can't be null")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(email)) {
                toast("email can't be null")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(pass)) {
                toast("password can't be null")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(confirmPass)) {
                toast("fill your confirmation password")
                return@setOnClickListener
            }

            if (pass.length < 6) {
                toast("your password must have 6 character and more")
                return@setOnClickListener
            }

            if (pass != confirmPass) {
                toast("your confirmation password is not same")
                return@setOnClickListener
            }

            register(auth!!, fullname, email, pass)
        }
    }

    private fun register(auth: FirebaseAuth, fullname: String, email: String, pass: String) {
        showLoading()
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                val uid = auth.currentUser!!.uid
                if (!task.isSuccessful) {
                    hud!!.dismiss()
                    toast("Registration Failed")
                } else {
                    val ref = FirebaseDatabase.getInstance().getReference("users/$uid")
                    val item = User(uid, fullname, email)
                    ref.setValue(item)
                        .addOnCompleteListener(this) {
                            hud!!.dismiss()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener(this) {
                            hud!!.dismiss()
                            toast(it.message!!)
                        }

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
