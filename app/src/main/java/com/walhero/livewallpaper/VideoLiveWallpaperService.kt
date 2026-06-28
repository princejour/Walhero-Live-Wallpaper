package com.walhero.livewallpaper

import android.media.MediaPlayer
import android.net.Uri
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder

class VideoLiveWallpaperService : WallpaperService() {
    override fun onCreateEngine(): Engine = VideoEngine()

    private inner class VideoEngine : Engine() {
        private var mediaPlayer: MediaPlayer? = null
        private var holderRef: SurfaceHolder? = null
        private var isVisibleToUser = false
        private var isPrepared = false

        override fun onSurfaceCreated(holder: SurfaceHolder) {
            super.onSurfaceCreated(holder)
            holderRef = holder
            preparePlayer(holder)
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            isVisibleToUser = visible
            val player = mediaPlayer
            if (visible) {
                if (player == null) {
                    holderRef?.let { preparePlayer(it) }
                } else if (isPrepared && !player.isPlaying) {
                    safeStart(player)
                }
            } else {
                if (player?.isPlaying == true) {
                    player.pause()
                }
            }
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
            holderRef = null
            releasePlayer()
        }

        override fun onDestroy() {
            releasePlayer()
            super.onDestroy()
        }

        private fun preparePlayer(holder: SurfaceHolder) {
            val savedUri = MainActivity.getSavedVideoUri(applicationContext) ?: return
            releasePlayer()
            isPrepared = false

            val player = MediaPlayer()
            try {
                player.setDataSource(applicationContext, Uri.parse(savedUri))
                player.setSurface(holder.surface)
                player.isLooping = true
                player.setVolume(0f, 0f)
                player.setOnPreparedListener { preparedPlayer ->
                    isPrepared = true
                    if (isVisibleToUser) {
                        safeStart(preparedPlayer)
                    }
                }
                player.setOnErrorListener { _, _, _ ->
                    releasePlayer()
                    true
                }
                mediaPlayer = player
                player.prepareAsync()
            } catch (_: Exception) {
                try {
                    player.release()
                } catch (_: Exception) {
                    // No action.
                }
                mediaPlayer = null
            }
        }

        private fun safeStart(player: MediaPlayer) {
            try {
                player.start()
            } catch (_: IllegalStateException) {
                releasePlayer()
            }
        }

        private fun releasePlayer() {
            val player = mediaPlayer
            mediaPlayer = null
            isPrepared = false
            try {
                player?.setOnPreparedListener(null)
                player?.setOnErrorListener(null)
                player?.stop()
            } catch (_: Exception) {
                // Ignore MediaPlayer lifecycle state errors.
            }
            try {
                player?.release()
            } catch (_: Exception) {
                // No action.
            }
        }
    }
}
