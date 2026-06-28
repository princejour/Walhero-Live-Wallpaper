package com.walhero.livewallpaper

import android.app.Activity
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.app_name)
        setContentView(buildContent())
        updateStatus()
    }

    private fun buildContent(): ScrollView {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(dp(22), dp(36), dp(22), dp(36))
            setBackgroundColor(Color.rgb(248, 250, 252))
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val title = TextView(this).apply {
            text = "Walhero Live Wallpaper"
            textSize = 26f
            setTextColor(Color.rgb(15, 23, 42))
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, dp(10))
        }

        val subtitle = TextView(this).apply {
            text = "اختار فيديو من الهاتف وخلّيه خلفية متحركة."
            textSize = 16f
            setTextColor(Color.rgb(71, 85, 105))
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, dp(22))
        }

        statusText = TextView(this).apply {
            textSize = 15f
            setTextColor(Color.rgb(30, 41, 59))
            gravity = Gravity.CENTER
            setPadding(dp(12), dp(12), dp(12), dp(12))
        }

        val chooseButton = Button(this).apply {
            text = "اختيار فيديو"
            setOnClickListener { openVideoPicker() }
        }

        val setButton = Button(this).apply {
            text = "تعيين كخلفية متحركة"
            setOnClickListener { openWallpaperSetter() }
        }

        val batteryButton = Button(this).apply {
            text = "إعدادات البطارية في OPPO"
            setOnClickListener { openBatterySettings() }
        }

        val note = TextView(this).apply {
            text = "ملاحظة: في بعض هواتف OPPO يلزم السماح للتطبيق بالعمل في الخلفية حتى لا تتوقف الخلفية."
            textSize = 14f
            setTextColor(Color.rgb(100, 116, 139))
            gravity = Gravity.CENTER
            setPadding(0, dp(16), 0, 0)
        }

        root.addView(title)
        root.addView(subtitle)
        root.addView(statusText, matchWrap())
        root.addView(chooseButton, buttonParams())
        root.addView(setButton, buttonParams())
        root.addView(batteryButton, buttonParams())
        root.addView(note, matchWrap())

        return ScrollView(this).apply { addView(root) }
    }

    private fun openVideoPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "video/*"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        }
        startActivityForResult(intent, REQUEST_VIDEO)
    }

    @Deprecated("Deprecated in platform API, kept intentionally to avoid external dependencies.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_VIDEO && resultCode == RESULT_OK) {
            val uri = data?.data ?: return
            val takeFlags = data.flags and Intent.FLAG_GRANT_READ_URI_PERMISSION
            try {
                contentResolver.takePersistableUriPermission(uri, takeFlags)
            } catch (_: SecurityException) {
                // Some file managers may not grant persistent permission.
            }
            prefs().edit().putString(KEY_VIDEO_URI, uri.toString()).apply()
            updateStatus()
            Toast.makeText(this, "تم اختيار الفيديو", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openWallpaperSetter() {
        if (getSavedVideoUri(this).isNullOrBlank()) {
            Toast.makeText(this, "اختار فيديو قبل تعيين الخلفية", Toast.LENGTH_LONG).show()
            return
        }
        val component = ComponentName(this, VideoLiveWallpaperService::class.java)
        val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
            putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, component)
        }
        try {
            startActivity(intent)
        } catch (_: Exception) {
            startActivity(Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER))
        }
    }

    private fun openBatterySettings() {
        try {
            startActivity(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
        } catch (_: Exception) {
            startActivity(Intent(Settings.ACTION_SETTINGS))
        }
    }

    private fun updateStatus() {
        val selected = getSavedVideoUri(this)
        statusText.text = if (selected.isNullOrBlank()) {
            "لم يتم اختيار فيديو بعد."
        } else {
            "الفيديو محفوظ وجاهز للتعيين."
        }
    }

    private fun prefs() = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    private fun matchWrap(): LinearLayout.LayoutParams = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply { bottomMargin = dp(14) }

    private fun buttonParams(): LinearLayout.LayoutParams = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply { topMargin = dp(10) }

    companion object {
        private const val REQUEST_VIDEO = 2101
        const val PREFS_NAME = "walhero_live_wallpaper_prefs"
        const val KEY_VIDEO_URI = "video_uri"

        fun getSavedVideoUri(context: Context): String? {
            return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString(KEY_VIDEO_URI, null)
        }
    }
}
