package com.example.mystoryapp.view.main

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.R
import com.example.mystoryapp.data.model.AccountPreference
import com.example.mystoryapp.data.remote.response.ListStoryItem
import com.example.mystoryapp.databinding.ActivityMainBinding
import com.example.mystoryapp.view.ViewModelFactory
import com.example.mystoryapp.view.adapter.StoryAdapter
import com.example.mystoryapp.view.detailstory.DetailStoryActivity
import com.example.mystoryapp.view.login.LoginActivity
import com.example.mystoryapp.view.login.LoginViewModel


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(2000)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)
        setContentView(binding.root)
        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel(){
        mainViewModel = ViewModelProvider(this, ViewModelFactory(AccountPreference.getInstance(dataStore)))[MainViewModel::class.java]
        mainViewModel.listStory.observe(this){
            setStoryData(it)
        }
        mainViewModel.isLoading.observe(this){
            showLoading(it)
        }
    }
    private fun setupAction(){
        var token: String
        mainViewModel.getUser().observe(this){
            if (it.isLogin){
                Toast.makeText(this, "Hello ${it.name}", Toast.LENGTH_SHORT).show()
                token = "Bearer ${it.token}"
                mainViewModel.getStory(token)
                binding.ivLogout.setOnClickListener {
                    mainViewModel.logoutUser()
                }
            }
            else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
    private fun setStoryData(stories: List<ListStoryItem>){
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        val storyAdapter = StoryAdapter(stories)
        binding.rvStory.adapter = storyAdapter
        storyAdapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback{
            override fun onItemClicked(accountClicked: ListStoryItem) {
                val i = Intent(this@MainActivity, DetailStoryActivity::class.java)
                i.putExtra(DetailStoryActivity.EXTRA_NAME, accountClicked.name)
                i.putExtra(DetailStoryActivity.EXTRA_PHOTO, accountClicked.photoUrl)
                i.putExtra(DetailStoryActivity.EXTRA_DATE, accountClicked.createdAt)
                i.putExtra(DetailStoryActivity.EXTRA_DESC, accountClicked.description)
                startActivity(i)
            }
        })
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}