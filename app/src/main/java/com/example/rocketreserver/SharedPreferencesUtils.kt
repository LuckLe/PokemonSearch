package com.example.rocketreserver

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object SharedPreferencesUtils {
    private const val FIRST_OPEN = "FIRST_OPEN"
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        preferences = EncryptedSharedPreferences.create(
            context,
            "secret_shared_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }


    fun removeIsFirstOpen() {
        preferences.edit().apply {
            remove(FIRST_OPEN)
            apply()
        }
    }

    fun getIsFirstOpen(): Boolean {
        return preferences.getBoolean(FIRST_OPEN, true)
    }

    fun setIsFirstOpen(b: Boolean) {
        preferences.edit().apply {
            putBoolean(FIRST_OPEN, b)
            apply()
        }
    }
}
