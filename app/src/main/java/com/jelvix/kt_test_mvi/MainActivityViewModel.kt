package com.jelvix.kt_test_mvi

import android.os.Parcelable
import android.util.Log
import androidx.annotation.Keep
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val IN_APP_MESSAGES = "in_app_messages"

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    interface UiState
    interface UiEvent
    interface UiEffect

    sealed class State : UiState {
        object Idle : State()
        object Loading : State()
        data class Error(val exception: CallErrors) : State()

        data class ResultUsersList(val data: String) : State()
        data class ResultSingleUserList(val data: String) : State()
    }

    sealed class Event : UiEvent {
        object OnShowToastClicked : Event()
        object OnWriteLogClicked : Event()
        object LoadUsersList : Event()
        object LoadSingleUserInfo : Event()
    }

    sealed class Effect : UiEffect {
        object ShowToast : Effect()
        object WriteLog : Effect()
    }

    private val _uiState = MutableStateFlow<UiState>(State.Idle)
    val uiState: MutableStateFlow<UiState> = _uiState

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent

    private val _uiEffect = MutableSharedFlow<UiEffect>(replay = 0)
    val uiEffect: SharedFlow<UiEffect> = _uiEffect

    init {
        Log.d("debapp", "VM init")
        subscribeEvents()
    }

    fun setState(state: UiState) {
        val newState = state
        viewModelScope.launch { _uiState.value = newState }
    }

    fun setEvent(event: UiEvent) {
        val newEvent = event
        viewModelScope.launch { _uiEvent.emit(newEvent) }
    }

    fun setEffect(effect: UiEffect) {
        val newEffect = effect
        viewModelScope.launch { _uiEffect.emit(newEffect) }
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            uiEvent.collect {
                handleEvent(it)
            }
        }
    }

    private fun handleEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is Event.LoadUsersList -> {
            }
            is Event.LoadSingleUserInfo -> {
            }
            is Event.OnShowToastClicked -> {
                setEffect(Effect.ShowToast)
            }
            is Event.OnWriteLogClicked -> {
                setEffect(Effect.WriteLog)
            }
        }
    }
}