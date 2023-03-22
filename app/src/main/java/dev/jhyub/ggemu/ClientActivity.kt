package dev.jhyub.ggemu

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import dev.jhyub.ggemu.ui.theme.GgemuTheme
import kotlinx.coroutines.*

class ClientActivity : ComponentActivity() {
    var client: Client? = null
    var loopJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ip = intent.getStringExtra("IP")
        val port = intent.getIntExtra("PORT", 11076)

        Gyro.setSensor(context = applicationContext)

        loopJob = CoroutineScope(Dispatchers.IO).launch {
            client = Client(ip ?: "", port)
            try {
                client?.connect()
            } catch (e: Exception) {
                client?.close()
                Log.e("ClientActivity", "Exiting, failed to connect to server")
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, e.localizedMessage ?: "Unknown error", Toast.LENGTH_SHORT).show()
                }
                this@ClientActivity.finish()
            }
            client?.loop()
        }

        setContent {
            GgemuTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        ClickButton(client)
                        DoubleClickButton(client)
                    }
                }
            }
        }
    }

    override fun finish() {
        super.finish()
        loopJob?.cancel()
        CoroutineScope(Dispatchers.IO).launch {
            client?.close()
        }
    }
}

@Composable
fun ClickButton(client: Client?) {
    Button(
        onClick = {
            Gyro.makeMessage(MessageType.CLICK)?.let {
                client?.pushSpecialMessage(it)
            }
        }
    ) {
        Text("Click")
    }
}

@Composable
fun DoubleClickButton(client: Client?) {
    Button(
        onClick = {
            Gyro.makeMessage(MessageType.DOUBLE_CLICK)?.let {
                client?.pushSpecialMessage(it)
            }
        }
    ) {
        Text("DoubleClick")
    }
}
