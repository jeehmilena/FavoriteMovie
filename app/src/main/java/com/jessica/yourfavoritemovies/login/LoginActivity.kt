package com.jessica.yourfavoritemovies.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jessica.yourfavoritemovies.MovieUtil
import com.jessica.yourfavoritemovies.R
import com.jessica.yourfavoritemovies.home.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private val viewModel: AuthenticationViewModel by lazy {
        ViewModelProvider(this).get(
            AuthenticationViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        bt_login.setOnClickListener {
            val email = etv_email.text.toString()
            val password = etv_password.text.toString()

            when {
                MovieUtil.validateEmailPassword(email, password) -> {
                    viewModel.loginEmail(email, password)
                }
            }
        }

        tv_login_register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        initViewModel()
    }

    private fun initViewModel() {
        viewModel.statusLogin.observe(this, Observer { status ->
            status?.let {
                navigateToHome(it)
            }
        })
    }

    private fun navigateToHome(status: Boolean) {
        when {
            status -> {
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }
    }
}