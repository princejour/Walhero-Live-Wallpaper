package com.walhero.livewallpaper.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.walhero.livewallpaper.data.VideoRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = VideoRepository(application)

    private val _videoUri = MutableStateFlow<Uri?>(null)
    val videoUri: StateFlow<Uri?> = _videoUri.asStateFlow()

    private val _soundEnabled = MutableStateFlow(true)
    val soundEnabled: StateFlow<Boolean> = _soundEnabled.asStateFlow()

    fun setVideoUri(uri: Uri) {
        viewModelScope.launch {
            _videoUri.value = uri
            repository.saveVideoUri(uri.toString())
        }
    }

    fun clearVideoUri() {
        viewModelScope.launch {
            _videoUri.value = null
            repository.clearVideoUri()
        }
    }

    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            _soundEnabled.value = enabled
            repository.setSoundEnabled(enabled)
        }
    }
}
