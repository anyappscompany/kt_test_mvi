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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
private const val IN_APP_MESSAGES = "in_app_messages"
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    sealed class Intent{
        object LoadUsersList: Intent()
        object LoadSingleUserInfo: Intent()
    }

    @Keep
    sealed class State(): Parcelable {
        @Keep
        @Parcelize
        data class ResultUsersList(val data: String) : State(), Parcelable
        @Keep
        @Parcelize
        data class ResultSingleUser(val data: String) : State(), Parcelable
        @Keep
        @Parcelize
        data class Error(val exception: CallErrors) : State(), Parcelable
        @Keep
        @Parcelize
        object Loading : State(), Parcelable
        @Keep
        @Parcelize
        object Default: State(), Parcelable
    }

    val intentChannel = Channel<Intent>(Channel.UNLIMITED)

    /*private val _state = MutableStateFlow<State>(State.Default)
    val state: StateFlow<State>
        get() = _state*/

    private val _state: MutableStateFlow<State?> =
        savedStateHandle.getStateFlow(IN_APP_MESSAGES, viewModelScope, State.Default)
    val state: StateFlow<State> = _state as StateFlow<State>

    init {
        Log.d("debapp", "VM init")
        handleIntents()
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect {
                when (it) {
                    Intent.LoadUsersList -> {
                        _state.value = State.Loading
                        _state.value = State.ResultUsersList("{name: \"Иван\", age: 25}")
                    }
                    Intent.LoadSingleUserInfo -> {
                        _state.value = State.Loading
                        _state.value = State.Error(CallErrors.ErrorServer)
                    }
                }
            }
        }
    }
}