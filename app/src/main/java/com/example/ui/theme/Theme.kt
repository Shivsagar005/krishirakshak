package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = AgriGreenAccent,
  onPrimary = AgriGreenDark,
  primaryContainer = AgriGreenMedium,
  onPrimaryContainer = TextPrimaryDark,
  secondary = HarvestGold,
  onSecondary = Color.Black,
  background = AgriBackgroundDark,
  surface = AgriSurfaceDark,
  onBackground = TextPrimaryDark,
  onSurface = TextPrimaryDark,
  surfaceVariant = AgriCardDark,
  onSurfaceVariant = TextSecondaryDark,
  outline = AgriBorderDark
)

private val LightColorScheme = lightColorScheme(
  primary = AgriGreenPrimary,
  onPrimary = Color.White,
  primaryContainer = AgriGreenContainer,
  onPrimaryContainer = AgriGreenDark,
  secondary = HarvestGold,
  onSecondary = Color.Black,
  background = AgriSurfaceLight,
  surface = AgriSurfaceCard,
  onBackground = TextPrimaryLight,
  onSurface = TextPrimaryLight,
  surfaceVariant = AgriSurfaceLight,
  onSurfaceVariant = TextSecondaryLight,
  outline = AgriBorderLight
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use our handcrafted high-polish agriculture palette
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

