package com.example.storyapp.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.model.DicodingStoryBasicResponse
import com.example.storyapp.model.RegisterRequest
import com.example.storyapp.model.Result
import com.example.storyapp.ui.component.TextInputEditTextCustom
import com.example.storyapp.ui.component.TextInputEditTextEmail
import com.example.storyapp.ui.component.TextInputEditTextPassword
import com.example.storyapp.ui.login.LoginActivity
import com.google.android.material.button.MaterialButton

class RegisterActivity : AppCompatActivity() {
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var registerViewModel: RegisterViewModel

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var inputName: TextInputEditTextCustom
    private lateinit var inputEmail: TextInputEditTextEmail
    private lateinit var inputPassword: TextInputEditTextPassword
    private lateinit var btnRegister: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance()
        val vm: RegisterViewModel by viewModels {
            factory
        }
        registerViewModel = vm

        setupUI()

        setupAnimation()

        setButtonRegisterListener()
    }

    private fun setupAnimation() {
        imageView.alpha = 0f
        textView.alpha = 0f
        btnRegister.alpha = 0f
        textView.alpha = 0f
        inputName.alpha = 0f
        inputEmail.alpha = 0f
        inputPassword.alpha = 0f

        val registerButtonAnimator =
            ObjectAnimator.ofFloat(btnRegister, View.ALPHA, 1f).setDuration(1500)
        val imageViewAnimator = ObjectAnimator.ofFloat(imageView, View.ALPHA, 1f).setDuration(2000)
        val textViewAnimator = ObjectAnimator.ofFloat(textView, View.ALPHA, 1f).setDuration(2000)
        val inputNameAnimator = ObjectAnimator.ofFloat(inputName, View.ALPHA, 1f).setDuration(2000)
        val inputEmailAnimator =
            ObjectAnimator.ofFloat(inputEmail, View.ALPHA, 1f).setDuration(2000)
        val inputPasswordAnimator =
            ObjectAnimator.ofFloat(inputPassword, View.ALPHA, 1f).setDuration(1500)

        AnimatorSet().apply {
            playTogether(
                imageViewAnimator,
                textViewAnimator,
                inputNameAnimator,
                inputEmailAnimator,
                inputPasswordAnimator,
                registerButtonAnimator
            )
            start()
        }
    }

    private fun setupUI() {
        imageView = binding.imageView
        textView = binding.textView
        inputName = binding.tietName
        inputEmail = binding.tietEmail
        inputPassword = binding.tietPassword
        btnRegister = binding.btnRegister
    }

    private fun isFormValid(): Boolean {
        return inputName.isInputValid() && inputEmail.isInputValid() && inputPassword.isInputValid()
    }

    private fun setButtonRegisterListener() {
        btnRegister.setOnClickListener {
            if (isFormValid()) {
                val name = inputName.getInputValue()
                val email = inputEmail.getInputValue()
                val password = inputPassword.getInputValue()

                val registerRequest =
                    RegisterRequest(name = name, email = email, password = password)
                registerViewModel.doRegister(registerRequest).observe(this) { result ->
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            setupRegisterByResponse(result.data)
                        }
                        is Result.Error -> {
                            showLoading(false)
                            toast(result.error)
                        }
                    }
                }
            } else if (!inputName.isInputValid()) {
                inputName.textInputEditText.requestFocus()
            } else if (!inputEmail.isInputValid()) {
                inputEmail.textInputEditText.requestFocus()
            } else {
                inputPassword.textInputEditText.requestFocus()
            }
        }
    }

    private fun toast(it: String) {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    }

    private fun setupRegisterByResponse(dicodingStoryBasicResponse: DicodingStoryBasicResponse) {
        val message = dicodingStoryBasicResponse.message
        val error = dicodingStoryBasicResponse.error
        toast(message)
        if (!error) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}