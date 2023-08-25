package com.crazycats.weathercompose.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)


val Thunderstorm = Color(0xFF1361FD)
val Drizzle = Color(0xFF526FA8)
val Rain = Color(0xFF0E8EFF)
val Snow = Color(0xFF987CFF)
val Atmosphere = Color(0xFFA8B4CC)
val Clear = Color(0xFFFF9D6F)
val Clouds = Color(0xFF454A4C)


val Rain_b = Brush.linearGradient(listOf(Rain, Color(0xFF003869)))
val Thunderstorm_b = Brush.linearGradient(listOf(Thunderstorm, Color(0xFF001795)))
val Drizzle_b = Brush.linearGradient(listOf(Drizzle, Color(0xFF26334E)))
val Snow_b = Brush.linearGradient(listOf(Snow, Color(0xFF4E4081)))
val Atmosphere_b = Brush.linearGradient(listOf(Atmosphere, Color(0xFF525863)))
val Clear_b = Brush.linearGradient(listOf(Clear, Color(0xFF81513A)))
val Unknown_b = Brush.linearGradient(listOf(Clouds, Color(0xFF181A1B)))
val Clouds_b = Brush.linearGradient(listOf(Color.White, Color.Black))