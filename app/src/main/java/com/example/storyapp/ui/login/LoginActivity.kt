package com.example.storyapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.model.DicodingStoryLoginResponse
import com.example.storyapp.model.LoginRequest
import com.example.storyapp.model.Result
import com.example.storyapp.ui.component.TextInputEditTextEmail
import com.example.storyapp.ui.component.TextInputEditTextPassword
import com.example.storyapp.ui.main.MainActivity
import com.example.storyapp.ui.register.RegisterActivity
import com.example.storyapp.utils.PrefUtils
import com.example.storyapp.utils.PrefUtils.Companion.PREF_NAME
import com.example.storyapp.utils.PrefUtils.Companion.PREF_TOKEN
import com.example.storyapp.utils.PrefUtils.Companion.PREF_USER_ID
import com.google.android.material.button.MaterialButton

class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var inputEmail: TextInputEditTextEmail
    private lateinit var inputPassword: TextInputEditTextPassword
    private lateinit var btnLogin: MaterialButton
    private lateinit var btnRegister: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance()
        val vm: LoginViewModel by viewModels {
            factory
        }
        loginViewModel = vm

        setupUI()

        setButtonLoginListener()
        setButtonRegisterListener()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun toast(it: String?) {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    }

    private fun setupLoginByResponse(dicodingStoryLoginResponse: DicodingStoryLoginResponse) {
        val message = dicodingStoryLoginResponse.message
        val error = dicodingStoryLoginResponse.error
        toast(message)
        if (!error) {
            val loginResult = dicodingStoryLoginResponse.loginResult
            updateSharePreference(loginResult.name, loginResult.userId, loginResult.token)
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun updateSharePreference(name: String, userId: String, token: String) {
        val prefUtils = PrefUtils(this)
        prefUtils.saveString(PREF_NAME, name)
        prefUtils.saveString(PREF_USER_ID, userId)
        prefUtils.saveString(PREF_TOKEN, token)
    }

    private fun setupUI() {
        inputEmail = binding.tietEmail
        inputPassword = binding.tietPassword
        btnLogin = binding.btnLogin
        btnRegister = binding.btnRegister
    }

    private fun setButtonLoginListener() {
        btnLogin.setOnClickListener {
            if (isFormValid()) {
                val email = inputEmail.getInputValue()
                val password = inputPassword.getInputValue()

                val loginRequest = LoginRequest(email = email, password = password)
                loginViewModel.doLogin(loginRequest).observe(this) { result ->
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            setupLoginByResponse(result.data)
                        }
                        is Result.Error -> {
                            showLoading(false)
                            toast(result.error)
                        }
                    }
                }
            } else if (!inputEmail.isInputValid()) {
                inputEmail.textInputEditText.requestFocus()
            } else {
                inputPassword.textInputEditText.requestFocus()
            }
        }
    }

    private fun setButtonRegisterListener() {
        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isFormValid(): Boolean {
        return inputEmail.isInputValid() && inputPassword.isInputValid()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}