package com.jelvix.kt_test_mvi

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import com.jelvix.kt_test_mvi.ui.theme.Kt_test_mviTheme

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.jelvix.kt_test_mvi.interfaces.UiState
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*

class MainActivity : ComponentActivity() {

    private val mainActivityViewModel: MainActivityViewModel by viewModels()
    // val textFieldState = remember { mutableStateOf(TextFieldValue()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        lifecycleScope.launchWhenStarted {
            mainActivityViewModel.uiEffect.collect {
                when (it) {
                    is MainActivityViewModel.Effect.WriteLog -> {
                        Log.d("debapp", "Write log 007")
                    }
                    is MainActivityViewModel.Effect.ShowToast -> {
                        Toast.makeText(applicationContext, "It is toast", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            mainActivityViewModel.uiState.collect {
                when (it) {
                    is MainActivityViewModel.State.Loading -> {
                        Log.d("debapp", "Loading")
                    }
                    is MainActivityViewModel.State.SuccessUsersList -> {
                        Log.d("debapp", "Show all users: ${it.data}")
                    }
                    is MainActivityViewModel.State.SuccessSingleUserList -> {
                        Log.d("debapp", "Single user: ${it.data}")
                    }
                    is MainActivityViewModel.State.Error -> {
                        Log.d("debapp", "Request with error: ${it.exception.toString()}")
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
    val state: UiState by mainActivityViewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(content = {

        Column(Modifier.fillMaxHeight()) {
            Button(onClick = {
                coroutineScope.launch {
                    mainActivityViewModel.setEvent(MainActivityViewModel.Event.OnWriteLogClicked)
                }
            }, modifier = Modifier.offset(y = 8.dp)) {
                Text(text = "WriteLog")
            }

            Button(onClick = {
                coroutineScope.launch {
                    mainActivityViewModel.setEvent(MainActivityViewModel.Event.OnShowToastClicked)
                }
            }, modifier = Modifier.offset(y = 16.dp)) {
                Text(text = "Show Toast")
            }

            Divider(
                Modifier
                    .height(32.dp)
                    .offset(y = 32.dp)
            )

            Button(onClick = {
                coroutineScope.launch {
                    mainActivityViewModel.setEvent(MainActivityViewModel.Event.LoadUsersList)
                }
            }, modifier = Modifier.offset(y = 32.dp)) {
                Text(text = "Load users")
            }

            Button(onClick = {
                coroutineScope.launch {
                    mainActivityViewModel.setEvent(MainActivityViewModel.Event.LoadSingleUserInfo)
                }
            }, modifier = Modifier.offset(y = 64.dp)) {
                Text(text = "Show user info")
            }

            val uistate = state
            when (uistate) {
                is MainActivityViewModel.State.SuccessUsersList -> {
                    Log.d("debapp", "Show all users: ${uistate.data}")
                    Text(text = uistate.data, color = Color.Magenta)
                }
                is MainActivityViewModel.State.Error -> {
                    //Log.d("debapp","Show all users: ${uistate.data}")
                    Text(text = uistate.exception.toString(), color = Color.Red)
                }
            }
        }
    })
}
