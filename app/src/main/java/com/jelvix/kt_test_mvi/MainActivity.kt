package com.jelvix.kt_test_mvi

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenStarted {
            mainActivityViewModel.uiEffect.collect {
                when (it) {
                    is MainActivityViewModel.Effect.WriteLog -> {
                        Log.d("debapp", "1111")
                    }
                    is MainActivityViewModel.Effect.ShowToast -> {
                        Toast.makeText(applicationContext, "123", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
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
fun MainActivityScreen(mainActivityViewModel: MainActivityViewModel) {
    val coroutineScope = rememberCoroutineScope()
    Column(Modifier.fillMaxHeight()) {
        /*Button(onClick = {
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
        }*/

        Button(onClick = {
            coroutineScope.launch {
                mainActivityViewModel.setEvent(MainActivityViewModel.Event.OnWriteLogClicked)
            }
        }) {
            Text(text = "WriteLog")
        }

        Button(onClick = {
            coroutineScope.launch {
                mainActivityViewModel.setEvent(MainActivityViewModel.Event.OnShowToastClicked)
            }
        }, modifier = Modifier.offset(y=50.dp)) {
            Text(text = "Show Toast")
        }
    }
}

/*fun handleState(state: MainActivityViewModel.State?){
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
}*/

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
