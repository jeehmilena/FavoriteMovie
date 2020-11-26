package com.jessica.yourfavoritemovies.home.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.jessica.yourfavoritemovies.Constants.LANGUAGE_PT_BR
import com.jessica.yourfavoritemovies.R
import com.jessica.yourfavoritemovies.authentication.view.LoginActivity
import com.jessica.yourfavoritemovies.favorites.FavoritesActivity
import com.jessica.yourfavoritemovies.home.adapter.MovieAdapter
import com.jessica.yourfavoritemovies.home.viewmodel.HomeViewModel
import com.jessica.yourfavoritemovies.model.Result
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_login.*

class HomeActivity : AppCompatActivity() {
    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(
            HomeViewModel::class.java
        )
    }

    private val adapter: MovieAdapter by lazy {
       MovieAdapter(
           ArrayList(), this::favoriteMovie
       )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        rv_movies.adapter = adapter
        rv_movies.layoutManager = GridLayoutManager(this, 2)
        viewModel.getListMovies(LANGUAGE_PT_BR)

        initViewModel()
    }

    private fun initViewModel() {
        viewModel.stateList.observe(this, Observer { state ->
            state?.let {
                showListMovies(it as MutableList<Result>)
            }
        })

        viewModel.stateFavorite.observe(this, Observer { favorite ->
            favorite?.let {
                showMessageFavorite(it)
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

    private fun showListMovies(list: MutableList<Result>){
        adapter.updateList(list)
    }

    private fun favoriteMovie(result: Result){
        viewModel.salvarFavorito(result)
    }

    private fun showMessageFavorite(result: Result){
        Snackbar.make(
            rv_movies,
            "Movie added - ${result.title}",
            Snackbar.LENGTH_LONG
        ).show()

    }

    private fun showLoading(status: Boolean) {
        when {
            status -> {
                pb_movies.visibility = View.VISIBLE
            }
            else -> {
                pb_movies.visibility = View.GONE
            }
        }
    }

    private fun showErrorMessage(message: String) {
        Snackbar.make(rv_movies, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_favoritos) {
            startActivity(Intent(this, FavoritesActivity::class.java))
            return true
        }
        if (id == R.id.action_logout) {
            logout()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener { task: Task<Void?>? ->
                startActivity(
                    Intent(
                        this,
                        LoginActivity::class.java
                    )
                )
                finish()
            }
    }
}