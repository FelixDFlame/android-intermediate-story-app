package com.example.storyapp.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storyapp.R
import com.example.storyapp.adapter.ListStoryAdapter
import com.example.storyapp.adapter.LoadingStateAdapter
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.location.LocationActivity
import com.example.storyapp.ui.login.LoginActivity
import com.example.storyapp.ui.storyadd.StoryAddActivity
import com.example.storyapp.utils.PrefUtils
import com.example.storyapp.utils.PrefUtils.Companion.PREF_NAME
import com.example.storyapp.utils.PrefUtils.Companion.PREF_THEME
import com.example.storyapp.utils.PrefUtils.Companion.PREF_TOKEN
import com.example.storyapp.utils.PrefUtils.Companion.PREF_USER_ID
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private lateinit var rvStory: RecyclerView
    private lateinit var fabAddStory: FloatingActionButton
    private lateinit var listStoryAdapter: ListStoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val vm: MainViewModel by viewModels {
            factory
        }
        mainViewModel = vm

        fabAddStory = binding.fabAddStory

        rvStory = binding.rvStory

        setupUITheme()
        setupRecycleView()
        setFabListener()

        val prefUtils = PrefUtils(this)
        val token = prefUtils.getString(PREF_TOKEN)
        mainViewModel.getAllStory(token).observe(this@MainActivity) {
            if (it != null) {
                listStoryAdapter.submitData(lifecycle, it)
            }
        }
    }

    private fun setupRecycleView() {
        listStoryAdapter = ListStoryAdapter()

        listStoryAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                listStoryAdapter.retry()
            }
        )

        rvStory.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(binding.root.context)
            adapter = listStoryAdapter
        }
    }

    private fun setFabListener() {
        fabAddStory.setOnClickListener {
            val intent = Intent(this, StoryAddActivity::class.java)
            startActivity(intent)
        }
    }

    fun setupUITheme() {
        val prefUtils = PrefUtils(this)
        val themeMode = prefUtils.getInt(PREF_THEME, -1)

        if (themeMode == 2) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else if (themeMode == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else if (themeMode == -1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        menu?.add(Menu.NONE, 1, Menu.NONE, "Dark Theme")
        menu?.add(Menu.NONE, 2, Menu.NONE, "Light Theme")
        menu?.add(Menu.NONE, 3, Menu.NONE, "Follow system Theme")
        menu?.add(Menu.NONE, 4, Menu.NONE, "Locale Setting")
        menu?.add(Menu.NONE, 5, Menu.NONE, "Logout")
        inflater.inflate(R.menu.option_home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val prefUtils = PrefUtils(this)
        when (item.itemId) {
            1 -> {
                prefUtils.saveInt(PREF_THEME, 2)
                setupUITheme()
            }
            2 -> {
                prefUtils.saveInt(PREF_THEME, 1)
                setupUITheme()
            }
            3 -> {
                prefUtils.saveInt(PREF_THEME, -1)
                setupUITheme()
            }
            4 -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            5 -> {
                prefUtils.saveString(PREF_NAME, "")
                prefUtils.saveString(PREF_USER_ID, "")
                prefUtils.saveString(PREF_TOKEN, "")

                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            R.id.geolocation -> {
                showLocationPage()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showLocationPage() {
        val intentLocation = Intent(this, LocationActivity::class.java)
        startActivity(intentLocation)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}