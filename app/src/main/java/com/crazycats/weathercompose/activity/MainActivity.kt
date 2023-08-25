package com.crazycats.weathercompose.activity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crazycats.weathercompose.R
import com.crazycats.weathercompose.activity.domain.ActivityRepositoryImpl
import com.crazycats.weathercompose.activity.presentation.BaseState
import com.crazycats.weathercompose.model.WeatherData
import com.crazycats.weathercompose.screens.WeatherScreen
import com.crazycats.weathercompose.ui.Constants.PERMISSION_REQUEST_CODE
import com.crazycats.weathercompose.ui.delegate.viewModelCreator
import com.crazycats.weathercompose.ui.theme.WeatherComposeTheme
import com.crazycats.weathercompose.utilites.ApiUtilities
import com.crazycats.weathercompose.utilites.TrackingUtility
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : ComponentActivity(), EasyPermissions.PermissionCallbacks {

    private val viewModel by viewModelCreator {
        MainActivityViewModel(ActivityRepositoryImpl(ApiUtilities))
    }

    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProvider: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)

        checkCurrentLocation()
        stateSubscribe()
    }

    private fun stateSubscribe() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    when (state) {
                        is BaseState.Error -> showError(state.message)
                        is BaseState.Loading -> showLoading()
                        is BaseState.Working -> showWorking()
                        is BaseState.Success -> successResult(state.weatherData)
                    }
                }
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading() {}

    private fun showWorking() {}

    private fun successResult(weatherData: WeatherData) {
        setContent {
            WeatherComposeTheme {
                Box {
                    WeatherScreen(
                        weatherData,
                        onClickCitySync = { city -> viewModel.getCityWeather(city) },
                        onClickLocationSync = { checkCurrentLocation() })
                }
            }
        }
    }

    private fun checkCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions()
            return
        }
        fusedLocationProvider.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                viewModel.getCurrentLocationWeather(
                    location.latitude.toString(),
                    location.longitude.toString()
                )
            }
        }
    }

    private fun requestPermissions() {
        if (TrackingUtility.hasLocationPermissions(this)) {
            checkCurrentLocation()
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.you_need_accept_lock_permissions),
                PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.you_need_accept_lock_permissions),
                PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}
