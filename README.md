# Walhero Live Wallpaper

A premium Android application that allows users to set video files as live wallpapers with a beautiful, modern UI.

## Features

✨ **Premium Design** - Dark neon blue/cyan/purple aesthetic with glowing effects
🎬 **Video Selection** - Choose any video from device storage
🔊 **Sound Control** - Toggle audio on/off for the wallpaper
📱 **Live Wallpaper Service** - Native Android live wallpaper implementation
💾 **Persistent Storage** - Saves preferences using DataStore
🎨 **Jetpack Compose UI** - Modern declarative UI framework

## Technical Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with Repository pattern
- **Media**: ExoPlayer / Media3
- **Storage**: DataStore Preferences
- **Min SDK**: 26
- **Target SDK**: 34

## Project Structure

```
app/
├── src/main/
│   ├── kotlin/com/walhero/livewallpaper/
│   │   ├── ui/
│   │   │   ├── MainActivity.kt
│   │   │   ├── screens/
│   │   │   │   └── MainScreen.kt
│   │   │   └── theme/
│   │   │       ├── Theme.kt
│   │   │       └── Type.kt
│   │   ├── service/
│   │   │   └── WalheroLiveWallpaperService.kt
│   │   ├── data/
│   │   │   └── VideoRepository.kt
│   │   ├── viewmodel/
│   │   │   └── MainViewModel.kt
│   │   └── WalheroApp.kt
│   ├── res/
│   │   ├── values/
│   │   │   ├── strings.xml
│   │   │   ├── colors.xml
│   │   │   └── themes.xml
│   │   ├── xml/
│   │   │   ├── wallpaper.xml
│   │   │   ├── backup_schemes.xml
│   │   │   └── data_extraction_rules.xml
│   │   └── mipmap/
│   │       ├── ic_launcher.png
│   │       └── ic_launcher_round.png
│   └── AndroidManifest.xml
├── build.gradle.kts
└── proguard-rules.pro
```

## Screens

### Splash Screen
- Official app icon (Image 2)
- WBJ copyright branding (Image 1)
- Premium animation

### Main Screen
- App title and subtitle
- Video status indicator
- Video picker button
- Sound on/off toggle
- Apply wallpaper button
- Clear video button
- Android version info card

## Building the App

### Prerequisites
- Android Studio Hedgehog or later
- Gradle 8.1+
- JDK 11+

### Build Commands

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Build AAB for Google Play
./gradlew bundleRelease

# Run tests
./gradlew test
```

## Installation

1. Clone the repository
2. Open in Android Studio
3. Build and run on device or emulator
4. Grant necessary permissions for storage access

## Permissions

- `READ_EXTERNAL_STORAGE` - To access video files
- `BIND_WALLPAPER_SERVICE` - To apply live wallpaper

## Theme Colors

- **Primary**: #0EA5E9 (Cyan Blue)
- **Secondary**: #A78BFA (Purple)
- **Tertiary**: #06B6D4 (Turquoise)
- **Background**: #000D1F (Dark Navy)
- **Surface**: #001A33 (Dark Blue)
- **On Surface**: #E0F2FE (Light Cyan)

## Design Reference

The UI follows the premium dark theme design from the official Walhero UI reference with:
- Rounded corners (16dp)
- Glowing buttons and cards
- Smooth spacing and padding
- Professional gradient elements
- Elegant premium aesthetic

## Version

**Current Version**: 1.0.0

## License

© WBJ - All rights reserved

## Support

For issues and feature requests, please contact support.
