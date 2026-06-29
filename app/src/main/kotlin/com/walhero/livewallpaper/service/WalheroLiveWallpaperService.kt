package com.walhero.livewallpaper.service

import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class WalheroLiveWallpaperService : WallpaperService() {

    override fun onCreateEngine(): Engine {
        return WalheroWallpaperEngine()
    }

    private inner class WalheroWallpaperEngine : Engine() {
        private var exoPlayer: ExoPlayer? = null
        private var mediaPlayer: MediaPlayer? = null
        private var videoUri: String? = null
        private var soundEnabled: Boolean = true
        private var surfaceHolder: SurfaceHolder? = null

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)
            this.surfaceHolder = surfaceHolder
            initializePlayer()
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            if (visible) {
                exoPlayer?.play()
            } else {
                exoPlayer?.pause()
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            releasePlayer()
        }

        private fun initializePlayer() {
            try {
                exoPlayer = ExoPlayer.Builder(this@WalheroLiveWallpaperService).build().apply {
                    playWhenReady = true
                    repeatMode = com.google.android.exoplayer2.Player.REPEAT_MODE_ALL
                }

                // Load default video or saved video
                videoUri?.let {
                    val mediaItem = MediaItem.fromUri(Uri.parse(it))
                    exoPlayer?.setMediaItem(mediaItem)
                    exoPlayer?.prepare()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun releasePlayer() {
            exoPlayer?.release()
            exoPlayer = null
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }
}
