package com.example.storyapp.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.storyapp.databinding.ActivitySplashScreenBinding
import com.example.storyapp.ui.login.LoginActivity
import com.example.storyapp.ui.main.MainActivity
import com.example.storyapp.utils.PrefUtils

class SplashScreenActivity : AppCompatActivity() {
    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding!!

    private val ALPHA_INVISIBLE = 0f
    private val ALPHA_VISIBLE = 1f
    private val ANIMATION_DURATION = 1500L

    fun setupChangeScreen() {
        val prefUtils = PrefUtils(this)
        val token = prefUtils.getString(PrefUtils.PREF_TOKEN)
        if (token != "") {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val llWrapperSplashScreen = binding.wrapperSplashScreen
        llWrapperSplashScreen.alpha = ALPHA_INVISIBLE
        llWrapperSplashScreen
            .animate()
            .setDuration(ANIMATION_DURATION)
            .alpha(ALPHA_VISIBLE)
            .withEndAction {
                setupChangeScreen()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}