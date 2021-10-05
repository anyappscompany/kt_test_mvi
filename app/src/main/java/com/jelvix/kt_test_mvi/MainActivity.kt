package com.jelvix.kt_test_mvi

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jelvix.kt_test_mvi.ui.theme.Kt_test_mviTheme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityViewModel.state
            .onEach { state ->  handleState(state)}
            .launchIn(lifecycleScope)

        setContent {
            Kt_test_mviTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    //Greeting("Android")

                    MainActivityScreen(mainActivityViewModel)
                }
            }
        }
    }
}

@Composable
fun MainActivityScreen(mainActivityViewModel: MainActivityViewModel){
    val coroutineScope = rememberCoroutineScope()
    Column() {
        Button(onClick = {
            coroutineScope.launch {
                mainActivityViewModel.intentChannel.send(MainActivityViewModel.Intent.LoadUsersList)
            }
        }) {
            Text(text = "LoadUsersList")
        }
        Button(onClick = {
            coroutineScope.launch {
                mainActivityViewModel.intentChannel.send(MainActivityViewModel.Intent.LoadSingleUserInfo)
            }
        }) {
            Text(text = "LoadSingleUserInfo")
        }
    }
}

fun handleState(state: MainActivityViewModel.State){
    when(state){
        is MainActivityViewModel.State.Loading->{
            Log.d("debapp", "Loading")
        }
        is MainActivityViewModel.State.Error->{
            Log.d("debapp", "Error")
        }
        is MainActivityViewModel.State.ResultUsersList->{
            Log.d("debapp", "Success ${state.data}")
        }
    }
}

/*
@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Kt_test_mviTheme {
        Greeting("Android")
    }
}*/
