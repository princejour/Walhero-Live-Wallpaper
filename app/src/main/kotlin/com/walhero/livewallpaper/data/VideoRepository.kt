package com.walhero.livewallpaper.data

import android.content.Context
import android.net.Uri
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VideoRepository(context: Context) {
    private val dataStore = context.dataStore

    companion object {
        private val VIDEO_URI_KEY = stringPreferencesKey("video_uri")
        private val SOUND_ENABLED_KEY = stringPreferencesKey("sound_enabled")
    }

    val videoUri: Flow<String?> = dataStore.data.map { preferences ->
        preferences[VIDEO_URI_KEY]
    }

    val soundEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[SOUND_ENABLED_KEY]?.toBoolean() ?: true
    }

    suspend fun saveVideoUri(uri: String) {
        dataStore.edit { preferences ->
            preferences[VIDEO_URI_KEY] = uri
        }
    }

    suspend fun clearVideoUri() {
        dataStore.edit { preferences ->
            preferences.remove(VIDEO_URI_KEY)
        }
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[SOUND_ENABLED_KEY] = enabled.toString()
        }
    }
}
