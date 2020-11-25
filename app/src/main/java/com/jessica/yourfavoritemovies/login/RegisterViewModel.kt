package com.jessica.yourfavoritemovies.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.jessica.yourfavoritemovies.MovieUtil

class RegisterViewModel(application: Application) : AndroidViewModel(application){
    var loading: MutableLiveData<Boolean> = MutableLiveData()
    var error: MutableLiveData<String> = MutableLiveData()
    var statusRegister: MutableLiveData<Boolean> = MutableLiveData()

    fun registrarUsuario(email: String, password: String) {
        loading.value = true
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                loading.value = false
                if (task.isSuccessful) {
                    MovieUtil.saveUserId(
                        getApplication(),
                        FirebaseAuth.getInstance().currentUser?.uid
                    )
                    statusRegister.value = true
                } else {
                    errorMessage("Authentication Failed!")
                    statusRegister.value = false
                    loading.value = false
                }
            }
    }

    private fun errorMessage(message: String){
        error.value = message
    }
}