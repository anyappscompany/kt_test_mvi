package com.jelvix.kt_test_mvi

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    sealed class Intent{
        object LoadUsersList: Intent()
        object LoadSingleUserInfo: Intent()
    }

    sealed class Result<out T : Any> {
        data class ResultUsersList<out T : Any>(val data: T) : Result<T>()
        data class ResultSingleUser<out T : Any>(val data: T) : Result<T>()
        data class Error(val exception: CallErrors) : Result<Nothing>()
        object Loading : Result<Nothing>()
        object Default: Result<Nothing>()
    }

    val intentChannel = Channel<Intent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<Result<Any>>(Result.Default)
    val state: StateFlow<Result<Any>>
        get() = _state

    init {
        Log.d("debapp", "VM init")
        handleIntents()
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect {
                when (it) {
                    Intent.LoadUsersList -> {
                        _state.value = Result.Loading
                        _state.value = Result.ResultUsersList("{name: \"Иван\", age: 25}")
                    }
                    Intent.LoadSingleUserInfo -> {
                        _state.value = Result.Loading
                        _state.value = Result.Error(CallErrors.ErrorServer)
                    }
                }
            }
        }
    }
}