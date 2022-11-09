package com.example.storyapp.utils

import android.content.Context

class PrefUtils(context: Context, name: String = "felix_story_app") {
    private var sharedPref = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    fun saveString(key: String, value: String?) {
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun saveInt(key: String, value: Int) {
        val editor = sharedPref.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getString(key: String, default: String = ""): String = sharedPref.getString(key, "") ?: default
    fun getInt(key: String, default: Int = 0): Int = sharedPref.getInt(key, default)

    companion object {
        const val PREF_NAME = "PREF_NAME"
        const val PREF_TOKEN = "PREF_TOKEN"
        const val PREF_USER_ID = "PREF_USER_ID"
        const val PREF_THEME = "PREF_THEME"
    }

}