package com.canytech.firebaseauthentication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        tv_register_login.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        btn_register_register.setOnClickListener {
            validateRegisterDetails()
        }

        setupActionBar()
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_register_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }

        toolbar_register_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun validateRegisterDetails() {
        when {
            TextUtils.isEmpty(et_register_name.text.toString().trim() { it <= ' ' }) -> {
                Toast.makeText(
                    this@RegisterActivity,
                    R.string.error_msg_enter_first_name,
                    Toast.LENGTH_SHORT
                ).show()
            }

            TextUtils.isEmpty(et_register_last_name.text.toString().trim() { it <= ' ' }) -> {
                Toast.makeText(
                    this@RegisterActivity,
                    R.string.error_msg_enter_last_name,
                    Toast.LENGTH_SHORT
                ).show()
            }

            TextUtils.isEmpty(et_register_email.text.toString().trim() { it <= ' ' }) -> {
                Toast.makeText(
                    this@RegisterActivity,
                    R.string.error_msg_enter_email,
                    Toast.LENGTH_SHORT
                ).show()
            }

            TextUtils.isEmpty(et_register_password.text.toString().trim() { it <= ' ' }) -> {
                Toast.makeText(
                    this@RegisterActivity,
                    R.string.error_msg_enter_password,
                    Toast.LENGTH_SHORT
                ).show()
            }

            TextUtils.isEmpty(
                et_register_confirm_password.text.toString().trim() { it <= ' ' }) -> {
                Toast.makeText(
                    this@RegisterActivity,
                    R.string.error_msg_confirm_password,
                    Toast.LENGTH_SHORT
                ).show()
            }

            et_register_password.text.toString()
                .trim { it <= ' ' } != et_register_confirm_password.text.toString()
                .trim { it <= ' ' } -> {
                Toast.makeText(
                    this@RegisterActivity,
                    R.string.error_msg_password_and_confirm_password_mismatch,
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {
                val email: String = et_register_email.text.toString().trim { it <= ' ' }
                val password: String = et_register_password.text.toString().trim { it <= ' ' }

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            Toast.makeText(
                                this@RegisterActivity,
                                R.string.registery_successfull,
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra("user_id", firebaseUser.uid)
                            intent.putExtra("email_id", email)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}