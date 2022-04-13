package com.example.mystoryapp.view.detailstory

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivityDetailStoryBinding
import com.example.mystoryapp.view.login.LoginActivity
import com.example.mystoryapp.view.main.MainActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupView(){
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

        val name = intent.getStringExtra(EXTRA_NAME)
        val photoUrl = intent.getStringExtra(EXTRA_PHOTO)
        val desc = intent.getStringExtra(EXTRA_DESC)
        val date = intent.getStringExtra(EXTRA_DATE)

        binding.tvName.text = name
        binding.tvOneWord.text = name?.get(0).toString().uppercase()
        binding.tvDesc.text = desc

        val inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val at = getString(R.string.time_at)
        val outputPattern = "EEE, d MMM '$at' HH:mm"
        val inputFormatter = DateTimeFormatter.ofPattern(inputPattern, Locale.ENGLISH)
        val outputFormatter = DateTimeFormatter.ofPattern(outputPattern, Locale.ENGLISH)
        val inputDate = LocalDateTime.parse(date, inputFormatter)
        val outputDate = outputFormatter.format(inputDate)

        binding.tvTime.text = outputDate
        Glide.with(this).load(photoUrl).into(binding.ivStory)

        binding.ivIcBack.setOnClickListener {
            finish()
        }
    }
    companion object {
        const val EXTRA_PHOTO = "extra_age"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESC = "extra_desc"
        const val EXTRA_DATE = "extra_date"
    }
}