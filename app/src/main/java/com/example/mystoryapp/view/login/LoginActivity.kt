package com.example.mystoryapp.view.login

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.R
import com.example.mystoryapp.data.model.AccountModel
import com.example.mystoryapp.data.model.AccountPreference
import com.example.mystoryapp.databinding.ActivityLoginBinding
import com.example.mystoryapp.view.ViewModelFactory
import com.example.mystoryapp.view.main.MainActivity
import com.example.mystoryapp.view.regist.RegistActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnRegist.setOnClickListener {
            val intent = Intent(this, RegistActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
        setupView()
        setupViewModel()
        setUpAction()
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

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(AccountPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]
        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        loginViewModel.loginResult.observe(this) {
            val account = AccountModel(
                userId = it.userId,
                name = it.name,
                token = it.token,
                isLogin = true
            )
            setReceivedData(account)
        }
    }

    private fun setReceivedData(account: AccountModel) {
        loginViewModel.saveAccount(account)
    }

    private fun setUpAction() {
        setMyButtonEnable()
        binding.cvEdtEmailLogin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        binding.cvEdtPassLogin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        binding.cvLoginButton.setOnClickListener {
            val email = binding.cvEdtEmailLogin.text.toString()
            val password = binding.cvEdtPassLogin.text.toString()
            loginViewModel.loginAccount(email, password)
            loginViewModel.loginResponse.observe(this) {
                if (!it.error) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
        errorMsg()
    }

    private fun errorMsg() {
        loginViewModel.errorCode.observe(this) {
            if (it != -1) {
                when (it) {
                    400 -> {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.error_400),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    401 -> {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.error_401),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        Toast.makeText(
                            this,
                            "Error: $it",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun setMyButtonEnable() {
        val email = binding.cvEdtEmailLogin.text
        val pass = binding.cvEdtPassLogin.text
        binding.cvLoginButton.isEnabled =
            email != null && email.toString().isNotEmpty() && pass != null && pass.toString()
                .isNotEmpty()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}