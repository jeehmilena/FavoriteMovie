package com.jessica.yourfavoritemovies.favorites.view

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jessica.yourfavoritemovies.R
import com.jessica.yourfavoritemovies.favorites.adapter.FavoriteAdapter
import com.jessica.yourfavoritemovies.favorites.viewmodel.FavoriteViewModel
import com.jessica.yourfavoritemovies.model.Result
import kotlinx.android.synthetic.main.activity_favorites.*

class FavoritesActivity : AppCompatActivity() {
    private var resultRemove = Result()
    private val adapter: FavoriteAdapter by lazy {
        FavoriteAdapter(
            ArrayList(), this::removeFavoriteMovie
        )
    }

    private val viewModel: FavoriteViewModel by lazy {
        ViewModelProvider(this).get(
            FavoriteViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        rv_movies_favorites.adapter = adapter
        rv_movies_favorites.layoutManager = GridLayoutManager(this, 2)
        viewModel.getFavorites()
        initViewModel()
    }

    private fun removeFavoriteMovie(result: Result){
        viewModel.removeFavoriteClickListener(result)
    }

    private fun initViewModel() {
        viewModel.stateList.observe(this, Observer { state ->
            state?.let {
                showListFavorites(it as MutableList<Result>)
            }
        })

        viewModel.stateRemoveFavorite.observe(this, Observer { favorite ->
            favorite?.let {
                showMessageRemovedFavorite(it)
            }

        })
    }

    private fun showListFavorites(list: MutableList<Result>){
        adapter.removeItem(resultRemove)
        adapter.updateList(list)
    }

    private fun showMessageRemovedFavorite(result: Result){
        resultRemove = result
        Snackbar.make(
            rv_movies_favorites,
            "Removed film - ${result.title}",
            Snackbar.LENGTH_LONG
        ).show()
    }
}