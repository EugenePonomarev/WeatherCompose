package com.crazycats.weathercompose.screens

import androidx.compose.ui.graphics.Brush
import com.crazycats.weathercompose.R
import com.crazycats.weathercompose.ui.Constants.TIME_PATTERN
import com.crazycats.weathercompose.ui.theme.Atmosphere_b
import com.crazycats.weathercompose.ui.theme.Clear_b
import com.crazycats.weathercompose.ui.theme.Clouds_b
import com.crazycats.weathercompose.ui.theme.Drizzle_b
import com.crazycats.weathercompose.ui.theme.Rain_b
import com.crazycats.weathercompose.ui.theme.Snow_b
import com.crazycats.weathercompose.ui.theme.Thunderstorm_b
import com.crazycats.weathercompose.ui.theme.Unknown_b
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun setBackground(id: Int): Brush {
    when (id) {
        //Thunderstorm
        in 200..232 -> return Thunderstorm_b
        //Drizzle
        in 300..321 -> return Drizzle_b
        //Rain
        in 500..531 -> return Rain_b
        //Snow
        in 600..622 -> return Snow_b
        //Atmosphere
        in 701..781 -> return Atmosphere_b
        //Clear
        800 -> return Clear_b
        //Clouds
        in 801..804 -> return Clouds_b
        //unknown
        else -> return Unknown_b
    }
}

fun setIcon(id: Int): Int {
    when (id) {
        //Thunderstorm
        in 200..232 -> return R.drawable.ic_storm_weather
        //Drizzle
        in 300..321 -> return R.drawable.ic_few_clouds
        //Rain
        in 500..531 -> return R.drawable.ic_rainy_weather
        //Snow
        in 600..622 -> return R.drawable.ic_snow_weather
        //Atmosphere
        in 701..781 -> return R.drawable.ic_broken_clouds
        //Clear
        800 -> return R.drawable.ic_clear_day
        //Clouds
        in 801..804 -> return R.drawable.ic_cloudy_weather
        //unknown
        else -> return R.drawable.ic_unknown
    }
}

fun convertKelvinToCelsius(temp: Double): String {
    var intTemp = temp
    intTemp = intTemp.minus(273)
    return intTemp.toBigDecimal().setScale(1, RoundingMode.UP).toDouble().toString()
}

fun convertKelvinToCelsiusDouble(temp: Double): Double {
    var intTemp = temp
    intTemp = intTemp.minus(273)
    return intTemp.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
}

fun convertKelvinToCelsiusNumber(temp: Double): String {
    var intTemp = temp
    intTemp = intTemp.minus(273)
    return intTemp.toBigDecimal().setScale(1, RoundingMode.UP).toDouble().toInt().toString()
}

fun sunTime(ts: Long): String {
    val unix: Long = ts * 1000
    val dateFormat = SimpleDateFormat(TIME_PATTERN, Locale.getDefault())
    val dt = Date(unix)
    return dateFormat.format(dt)
}