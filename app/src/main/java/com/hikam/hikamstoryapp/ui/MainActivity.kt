package com.hikam.hikamstoryapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hikam.hikamstoryapp.R
import com.hikam.hikamstoryapp.data.ResultState
import com.hikam.hikamstoryapp.databinding.ActivityMainBinding
import com.hikam.hikamstoryapp.viewmodel.MainViewModel
import com.hikam.hikamstoryapp.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()

        binding.fabUpload.setOnClickListener {
            startActivity(Intent(this, CreateStoryActivity::class.java))
        }

        viewModel.getSession().observe(this) {
            if (!it.isLogin) {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                getData()
                setupAction()
            }
        }
        val layoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvReview.addItemDecoration(itemDecoration)
    }

    private fun setupAction() {
        lifecycleScope.launch {
            viewModel.getStories().observe(this@MainActivity) { story ->
                when (story) {
                    is ResultState.Success -> {
                        binding.progressBar.visibility = View.INVISIBLE
                    }
                    is ResultState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is ResultState.Error -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        val error = story.error
                        Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getData() {
        val adapter = MainAdapter()
        binding.rvReview.adapter = adapter.withLoadStateFooter(
            footer = PagingLoadStateAdapter {
                adapter.retry()
            }
        )
        viewModel.quote.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.maps -> {
                lifecycleScope.launch {
                    val intent = Intent(this@MainActivity, MapsActivity::class.java)
                    startActivity(intent)
                }
            }
            R.id.logout -> {
                lifecycleScope.launch {
                    viewModel.logout()
                    val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
