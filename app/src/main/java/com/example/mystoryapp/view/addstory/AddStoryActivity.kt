package com.example.mystoryapp.view.addstory

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivityAddStoryBinding

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val token = intent.getStringExtra(EXTRA_TOKEN)
        val name = intent.getStringExtra(EXTRA_NAME)
        binding.tvHi2.text = "$name!"
        setupView()
//        setupViewModel()
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

        binding.ivIcBack.setOnClickListener {
            finish()
        }
    }
    private fun setupAction(){

    }

    companion object {
        const val EXTRA_TOKEN = "extra_token"
        const val EXTRA_NAME = "extra_name"
    }

}