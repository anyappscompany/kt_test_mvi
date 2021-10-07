package com.jelvix.kt_test_mvi

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
                when(it){
                    is MainActivityViewModel.State.Loading->{
                        Log.d("debapp","Loading")
                    }
                    is MainActivityViewModel.State.ResultUsersList->{
                        Log.d("debapp","Show all users: ${it.data}")
                    }
                    is MainActivityViewModel.State.ResultSingleUserList->{
                        Log.d("debapp","Single user: ${it.data}")
                    }
                    is MainActivityViewModel.State.Error->{
                        Log.d("debapp","Request with error: ${it.exception.toString()}")
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
        Button(onClick = {
            coroutineScope.launch {
                mainActivityViewModel.setEvent(MainActivityViewModel.Event.OnWriteLogClicked)
            }
        }, modifier = Modifier.offset(y=8.dp)) {
            Text(text = "WriteLog")
        }

        Button(onClick = {
            coroutineScope.launch {
                mainActivityViewModel.setEvent(MainActivityViewModel.Event.OnShowToastClicked)
            }
        }, modifier = Modifier.offset(y=16.dp)) {
            Text(text = "Show Toast")
        }

        Divider(Modifier.height(32.dp).offset(y=32.dp))

        Button(onClick = {
            coroutineScope.launch {
                mainActivityViewModel.setEvent(MainActivityViewModel.Event.LoadUsersList)
            }
        }, modifier = Modifier.offset(y=32.dp)) {
            Text(text = "Load users")
        }

        Button(onClick = {
            coroutineScope.launch {
                mainActivityViewModel.setEvent(MainActivityViewModel.Event.LoadSingleUserInfo)
            }
        }, modifier = Modifier.offset(y=64.dp)) {
            Text(text = "Show user info")
        }
    }
}
