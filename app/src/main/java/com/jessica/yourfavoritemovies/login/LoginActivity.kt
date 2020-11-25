package com.jessica.yourfavoritemovies.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.jessica.yourfavoritemovies.MovieUtil.saveUserId
import com.jessica.yourfavoritemovies.R
import com.jessica.yourfavoritemovies.home.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        bt_login.setOnClickListener {
            loginEmail()
        }

        tv_login_register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

    }


    fun loginEmail() {
        val email: String = etv_email.text.toString()
        val password: String = etv_password.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Campos não podem ser vazios :(", Toast.LENGTH_SHORT).show()
            return
        }

        // tentamos fazer o login com o email e senha no firebase
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult?> ->

                // Caso login com sucesso vamos para tela  Home
                if (task.isSuccessful) {
                    irParaHome(FirebaseAuth.getInstance().currentUser!!.uid)
                } else {
                    // Se deu algum erro mostramos para o usuário a mensagem
                    Snackbar.make(bt_login, task.exception!!.message!!, Snackbar.LENGTH_LONG).show()
                }
            }
    }

    private fun irParaHome(uiid: String) {
        saveUserId(application.applicationContext, uiid)
        startActivity(Intent(applicationContext, HomeActivity::class.java))
        finish()
    }
}