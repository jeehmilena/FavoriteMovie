package com.jessica.yourfavoritemovies.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.jessica.yourfavoritemovies.MovieUtil.validateEmailPassword
import com.jessica.yourfavoritemovies.R
import com.jessica.yourfavoritemovies.home.HomeActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private val viewModel: RegisterViewModel by lazy {
        ViewModelProvider(this).get(
            RegisterViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_register.setOnClickListener {
            val name = etv_email_register.text.toString()
            val email = etv_email_register.text.toString()
            val password = etv_password_register.text.toString()

            if (validateEmailPassword(name, email, password)) {
                viewModel.registrarUsuario(email,password)
            }

            initViewModel()
        }
    }

    private fun initViewModel() {

        viewModel.statusRegister.observe(this, Observer { status ->
            status?.let {
               navigateToHome(it)
            }
        })

        viewModel.loading.observe(this, Observer { loading ->
            loading?.let {
                showLoading(it)
            }
        })

        viewModel.error.observe(this, Observer { loading ->
            loading?.let {
                showErrorMessage(it)
            }
        })
    }

    private fun navigateToHome(status: Boolean) {
       if (status){
           startActivity(Intent(this, HomeActivity::class.java))
       }
    }

    private fun showErrorMessage(message: String) {
        Snackbar.make(btn_register, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showLoading(status: Boolean) {
        when {
            status -> {
                pb_register.visibility = View.VISIBLE
            }
            else -> {
                pb_register.visibility = View.GONE
            }
        }
    }
}