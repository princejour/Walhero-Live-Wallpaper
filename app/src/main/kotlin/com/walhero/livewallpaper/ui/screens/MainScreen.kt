package com.walhero.livewallpaper.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.app.WallpaperManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.launch

val Context.dataStore by preferencesDataStore(name = "walhero_prefs")

@Composable
fun MainScreen(context: Context) {
    var videoUri by remember { mutableStateOf<Uri?>(null) }
    var soundEnabled by remember { mutableStateOf(true) }
    var isVideoSelected by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                videoUri = uri
                isVideoSelected = true
                coroutineScope.launch {
                    saveVideoUri(context, uri.toString())
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000D1F))
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        // App Title
        Text(
            text = "Walhero",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFE0F2FE),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Subtitle
        Text(
            text = "Live Wallpaper",
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            background = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF0EA5E9),
                    Color(0xFFA78BFA)
                )
            ),
            color = Color(0xFF0EA5E9),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "Premium Video Wallpaper Experience",
            fontSize = 13.sp,
            color = Color(0xFFB0E0FF),
            modifier = Modifier.padding(bottom = 30.dp)
        )

        // Status Card
        StatusCard(isVideoSelected = isVideoSelected)

        Spacer(modifier = Modifier.height(20.dp))

        // Choose Video Button
        PremiumButton(
            text = "Choose Video",
            icon = Icons.Default.VolumeUp,
            onClick = {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                videoPickerLauncher.launch(intent)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sound Toggle
        SoundToggleCard(
            soundEnabled = soundEnabled,
            onToggle = { soundEnabled = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Apply Wallpaper Button
        PremiumButton(
            text = "Apply Live Wallpaper",
            isGradient = true,
            onClick = {
                if (videoUri != null) {
                    applyWallpaper(context, videoUri!!, soundEnabled)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isVideoSelected
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Clear Video Button
        PremiumButton(
            text = "Clear Video",
            icon = Icons.Default.Close,
            onClick = {
                videoUri = null
                isVideoSelected = false
                coroutineScope.launch {
                    clearVideoUri(context)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isVideoSelected
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Info Card
        InfoCard()

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun StatusCard(isVideoSelected: Boolean) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        color = Color(0xFF001A33),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = if (isVideoSelected) Color(0xFF06B6D4) else Color(0xFF64748B),
                modifier = Modifier.size(32.dp)
            )

            Column {
                Text(
                    text = if (isVideoSelected) "Video ready" else "No video selected",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFE0F2FE)
                )
                Text(
                    text = if (isVideoSelected) "Your video is ready to set" else "Select a video to continue",
                    fontSize = 12.sp,
                    color = Color(0xFF06B6D4),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun PremiumButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: androidx.compose.material.icons.Icons? = null,
    isGradient: Boolean = false,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(14.dp))
            .shadow(8.dp, RoundedCornerShape(14.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isGradient) Color(0xFF0EA5E9) else Color(0xFF001A33),
            disabledContainerColor = Color(0xFF0D1B2A)
        ),
        enabled = enabled,
        shape = RoundedCornerShape(14.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isGradient) Color.White else Color(0xFFE0F2FE)
        )
    }
}

@Composable
fun SoundToggleCard(
    soundEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        color = Color(0xFF001A33),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (soundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                    contentDescription = null,
                    tint = Color(0xFF06B6D4),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Sound On / Off",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFE0F2FE)
                )
            }

            Switch(
                checked = soundEnabled,
                onCheckedChange = onToggle,
                modifier = Modifier,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF0EA5E9),
                    checkedTrackColor = Color(0xFF0EA5E9),
                    uncheckedThumbColor = Color(0xFF64748B),
                    uncheckedTrackColor = Color(0xFF1E293B)
                )
            )
        }
    }
}

@Composable
fun InfoCard() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        color = Color(0xFF0D1B2A),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = Color(0xFFA78BFA),
                modifier = Modifier
                    .size(24.dp)
                    .padding(top = 2.dp)
            )
            Text(
                text = "On some Android versions, the system may apply the wallpaper to home and lock screens together.",
                fontSize = 13.sp,
                color = Color(0xFFB0E0FF),
                lineHeight = 18.sp
            )
        }
    }
}

private suspend fun saveVideoUri(context: Context, uri: String) {
    val dataStore = context.dataStore
    // This would save to DataStore in a real implementation
}

private suspend fun clearVideoUri(context: Context) {
    val dataStore = context.dataStore
    // This would clear from DataStore in a real implementation
}

private fun applyWallpaper(context: Context, videoUri: Uri, soundEnabled: Boolean) {
    val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
    intent.putExtra(
        WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
        android.content.ComponentName(
            context,
            com.walhero.livewallpaper.service.WalheroLiveWallpaperService::class.java
        )
    )
    intent.putExtra("video_uri", videoUri.toString())
    intent.putExtra("sound_enabled", soundEnabled)
    context.startActivity(intent)
}
