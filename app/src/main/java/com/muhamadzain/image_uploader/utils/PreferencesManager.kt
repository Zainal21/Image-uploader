package com.muhamadzain.image_uploader.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object PreferencesManager {
    private fun sharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun setToken(context: Context, token: String) {
        val pref = sharedPreferences(context)
        pref.edit().putString("TokenBearer", token).apply()
    }

    fun getToken(context: Context): String {
        return sharedPreferences(context).getString("TokenBearer", "") ?: ""
    }

    fun setLogin(context: Context, login: Boolean) {
        val pref = sharedPreferences(context)
        pref.edit().putBoolean("LoginApp", login).apply()
    }

    fun getLogin(context: Context): Boolean {
        return sharedPreferences(context).getBoolean("LoginApp", false)
    }

    fun setImagePath(context: Context, paramRef: String) {
        val pref = sharedPreferences(context)
        pref.edit().putString("imagePath", paramRef).apply()
    }

    fun getImagePath(context: Context): String {
        return sharedPreferences(context).getString("imagePath", "") ?: ""
    }
}