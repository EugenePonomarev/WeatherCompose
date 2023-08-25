package com.crazycats.weathercompose.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crazycats.weathercompose.activity.domain.ActivityRepository
import com.crazycats.weathercompose.activity.presentation.BaseState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainActivityViewModel(private val activityRepository: ActivityRepository) : ViewModel() {

    private var _state = MutableStateFlow<BaseState>(BaseState.Working)
    val state = _state.asStateFlow()

    private fun updateState(onUpdate: (BaseState) -> BaseState) {
        _state.update { oldState -> onUpdate(oldState) }
    }

    fun getCurrentLocationWeather(latitude: String, longitude: String) {
        viewModelScope.launch {
            activityRepository.getCurrentLocationWeather(latitude, longitude)
                .onStart { updateState { BaseState.Loading } }
                .onCompletion { updateState { BaseState.Working } }
                .catch { updateState { BaseState.Error("Cannot get weather") } }
                .collect { data ->
                    updateState { BaseState.Success(data) }
                }
        }
    }

    fun getCityWeather(city: String) {
        viewModelScope.launch {
            activityRepository.getCityWeather(city)
                .onStart { updateState { BaseState.Loading } }
                .onCompletion { updateState { BaseState.Working } }
                .catch { updateState { BaseState.Error("Cannot get weather") } }
                .collect { data ->
                    updateState { BaseState.Success(data) }
                }
        }
    }
}