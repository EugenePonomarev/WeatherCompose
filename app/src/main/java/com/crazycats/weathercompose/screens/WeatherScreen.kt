package com.crazycats.weathercompose.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crazycats.weathercompose.R
import com.crazycats.weathercompose.model.WeatherData
import com.crazycats.weathercompose.ui.Constants
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WeatherScreen(
    weather: WeatherData,
    onClickCitySync: (city: String) -> Unit,
    onClickLocationSync: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(setBackground(weather.weather[0].id))
    ) {
        SearchView(weather, onClickLocationSync, onClickCitySync)
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            CurrentWeather(weather)
            DescriptionWeather(weather)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    weather: WeatherData,
    onClickLocationSync: () -> Unit,
    onClickCitySync: (city: String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val cities = remember { mutableStateListOf("Moscow", "London", "Kotelnich") }

    SearchBar(
        query = query,
        onQueryChange = { query = it },
        onSearch = {
            cities.add(it.trim())
            onClickCitySync.invoke(it.trim())
            active = false
        },
        active = active,
        onActiveChange = { active = it },
        placeholder = { Text(text = stringResource(id = R.string.search_here)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        trailingIcon = {
            Row {
                if (active) {
                    Icon(
                        modifier = Modifier.clickable {
                            if (query.isNotEmpty())
                                query = ""
                            else
                                active = false
                        },
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear Icon"
                    )
                }
                Icon(
                    modifier = Modifier
                        .padding(start = 4.dp, end = 4.dp)
                        .clickable {
                            onClickLocationSync.invoke()
                            query = weather.name
                            cities.add(query)
                            active = false
                        },
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "LocationOn Icon"
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        cities.forEach {
            Row(modifier = Modifier
                .padding(14.dp)
                .clickable {
                    val city = it.trim()
                    onClickCitySync.invoke(city)
                    query = city
                    active = false
                }) {
                Icon(
                    modifier = Modifier.padding(end = 10.dp),
                    imageVector = Icons.Default.History,
                    contentDescription = "Icon History"
                )
                Text(text = it.trim())
            }
        }
    }
}

@Composable
fun CurrentWeather(weather: WeatherData) {
    val currentDate by remember {
        mutableStateOf(SimpleDateFormat(Constants.DATE_PATTERN, Locale.getDefault()).format(Date()))
    }
    Column {
        Text(
            text = weather.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            fontSize = 20.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Text(
            text = currentDate.toString(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, top = 4.dp, end = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = stringResource(
                    id = R.string.min,
                    convertKelvinToCelsius(weather.main.temp_min)
                ),
                modifier = Modifier
                    .padding(top = 10.dp),
                color = Color.White
            )
            Text(
                text = stringResource(
                    id = R.string.max,
                    convertKelvinToCelsius(weather.main.temp_max)
                ),
                modifier = Modifier
                    .padding(top = 10.dp),
                color = Color.White
            )

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, top = 4.dp, end = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(
                        id = R.string.d,
                        convertKelvinToCelsiusNumber(weather.main.temp)
                    ),
                    modifier = Modifier
                        .padding(top = 10.dp),
                    color = Color.White,
                    fontSize = 85.sp
                )
                Text(
                    text = stringResource(
                        id = R.string.d,
                        convertKelvinToCelsiusNumber(weather.main.feels_like)
                    ),
                    modifier = Modifier
                        .padding(top = 10.dp),
                    color = Color.White
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    modifier = Modifier
                        .size(125.dp),
                    painter = painterResource(id = setIcon(weather.weather[0].id)),
                    contentDescription = "Weather Image",
                )
                Text(
                    text = weather.weather[0].main,
                    modifier = Modifier
                        .padding(top = 10.dp),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun DescriptionWeather(weather: WeatherData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.background(setBackground(weather.weather[0].id))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, top = 4.dp, end = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.pressure),
                        contentDescription = "Image Pressure",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(2.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Text(
                        text = weather.main.pressure.toString(),
                        modifier = Modifier
                            .padding(top = 10.dp),
                        color = Color.White,
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(id = R.string.pressure),
                        modifier = Modifier
                            .padding(top = 10.dp),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.humidity),
                        contentDescription = "Image Humidity",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(2.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Text(
                        text = stringResource(
                            id = R.string.percent,
                            weather.main.humidity.toString()
                        ),
                        modifier = Modifier
                            .padding(top = 10.dp),
                        color = Color.White,
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(id = R.string.humidity),
                        modifier = Modifier
                            .padding(top = 10.dp),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.wind),
                        contentDescription = "Image Wind",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(2.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Text(
                        text = stringResource(id = R.string.m_s, weather.wind.speed.toString()),
                        modifier = Modifier
                            .padding(top = 10.dp),
                        color = Color.White,
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(id = R.string.windy_speed),
                        modifier = Modifier
                            .padding(top = 10.dp),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, top = 4.dp, end = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sunrise),
                        contentDescription = "Image Sunrise",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(2.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Text(
                        text = sunTime(weather.sys.sunrise.toLong()),
                        modifier = Modifier
                            .padding(top = 10.dp),
                        color = Color.White,
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(id = R.string.sunrise),
                        modifier = Modifier
                            .padding(top = 10.dp),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sunset),
                        contentDescription = "Image Sunset",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(2.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Text(
                        text = sunTime(weather.sys.sunset.toLong()),
                        modifier = Modifier
                            .padding(top = 10.dp),
                        color = Color.White,
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(id = R.string.sunset),
                        modifier = Modifier
                            .padding(top = 10.dp),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_device_thermostat_24),
                        contentDescription = "Image Temperature F",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(2.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Text(
                        text = stringResource(
                            id = R.string.d,
                            ((convertKelvinToCelsiusDouble(weather.main.temp) * 1.8) + 32).toInt()
                                .toString()
                        ),
                        modifier = Modifier
                            .padding(top = 10.dp),
                        color = Color.White,
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(id = R.string.f),
                        modifier = Modifier
                            .padding(top = 10.dp),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ground_level),
                        contentDescription = "Image Ground",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(2.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Text(
                        text = weather.main.grnd_level.toString(),
                        modifier = Modifier
                            .padding(top = 10.dp),
                        color = Color.White,
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(id = R.string.ground),
                        modifier = Modifier
                            .padding(top = 10.dp),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.water),
                        contentDescription = "Image Sea",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(2.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Text(
                        text = weather.main.sea_level.toString(),
                        modifier = Modifier
                            .padding(top = 10.dp),
                        color = Color.White,
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(id = R.string.sea),
                        modifier = Modifier
                            .padding(top = 10.dp),
                        color = Color.White
                    )
                }
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.country),
                        contentDescription = "Image Country",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(2.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Text(
                        text = weather.sys.country,
                        modifier = Modifier
                            .padding(top = 10.dp),
                        color = Color.White,
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(id = R.string.country),
                        modifier = Modifier
                            .padding(top = 10.dp),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}