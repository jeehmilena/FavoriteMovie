package com.jessica.yourfavoritemovies

import android.content.Context
import android.util.Patterns

object MovieUtil {
    fun saveUserId(context: Context, uiid: String?) {
        val preferences = context.getSharedPreferences("APP", Context.MODE_PRIVATE)
        preferences.edit().putString("UIID", uiid).apply()
    }

    fun getUserId(context: Context): String? {
        val preferences = context.getSharedPreferences("APP", Context.MODE_PRIVATE)
        return preferences.getString("UIID", "")
    }

    fun validateEmailPassword(name: String, email: String, password: String): Boolean {
        return when {
            name.isEmpty() || email.isEmpty() || password.isEmpty() -> {
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                false
            }
            password.length < 6 -> {
                false
            }
            else -> true
        }
    }
}