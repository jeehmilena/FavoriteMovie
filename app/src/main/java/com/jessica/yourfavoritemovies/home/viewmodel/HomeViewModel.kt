package com.jessica.yourfavoritemovies.home.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.*
import com.jessica.yourfavoritemovies.Constants.FAVORITES_PATH
import com.jessica.yourfavoritemovies.MovieRepository
import com.jessica.yourfavoritemovies.MovieUtil.getUserId
import com.jessica.yourfavoritemovies.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MovieRepository()
    var stateList: MutableLiveData<List<Result>> = MutableLiveData()
    var error: MutableLiveData<String> = MutableLiveData()
    var loading: MutableLiveData<Boolean> = MutableLiveData()
    var stateFavorite: MutableLiveData<Result> = MutableLiveData()

    fun getListMovies(language: String) {
        viewModelScope.launch {
            loading.value = true
            try {
                val movieResult = withContext(Dispatchers.IO) {
                    repository.getMovies(language)
                }
                stateList.value = movieResult.results
                loading.value = false
            } catch (ex: Exception) {
                errorMessage("It looks like we had a problem. Try later!")
            } finally {
                loading.value = false
            }
        }
    }

    fun saveFavorite(result: Result) {
        val database = FirebaseDatabase.getInstance()
        val reference =
            database.getReference(getUserId(getApplication()).toString() + FAVORITES_PATH)

        reference.orderByKey().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var movieAdded = false

                for (resultSnapshot in dataSnapshot.children) {
                    val firebaseResult = resultSnapshot.getValue(Result::class.java)

                    when {
                        firebaseResult?.id != null && firebaseResult.id == result.id -> {
                            movieAdded = true
                        }
                    }
                }

                when {
                    movieAdded -> {
                        errorMessage("The movie has already been added!")
                    }
                    else -> {
                        saveMovieFavorited(reference, result)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun saveMovieFavorited(reference: DatabaseReference, result: Result) {
        val key = reference.push().key

        key?.let {
            reference.child(it).setValue(result)

            reference.child(key).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val movieResult = dataSnapshot.getValue(Result::class.java)
                    stateFavorite.value = movieResult
                }

                override fun onCancelled(error: DatabaseError) {
                    errorMessage(error.message)
                }
            })
        }

    }

    private fun errorMessage(message: String) {
        error.value = message
    }
}


