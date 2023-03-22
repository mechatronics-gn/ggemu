package dev.jhyub.ggemu

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.jhyub.ggemu.ui.theme.GgemuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GgemuTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Scaffold(
                        topBar = { TopAppBar { Text("GyroGun EMUlator") } },
                    ) { contentPadding ->
                        ConnectionLauncher()
                        Box(modifier = Modifier.padding(contentPadding))
                    }
                }
            }
        }
    }
}

@Composable
fun ConnectionLauncher() {
    var ip by rememberSaveable(key = "ip") { mutableStateOf("") }
    var port by rememberSaveable(key = "port") { mutableStateOf<Int?>(11076) }

    val context = LocalContext.current

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(20.dp))
        TextField (
            value = ip,
            label = { Text("Server IP") },
            singleLine = true,
            onValueChange = { ip = it }
        )
        Spacer(Modifier.height(10.dp))
        TextField (
            value = port?.toString() ?: "",
            label = { Text("Server Port") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            onValueChange = { port = it.toIntOrNull() }
        )
        Spacer(Modifier.height(15.dp))
        Button (
            onClick = {
                val intent =Intent(context, ClientActivity::class.java)
                intent.putExtra("IP", ip)
                intent.putExtra("PORT", port)
                context.startActivity(intent)
            },
        ) {
            Text("Connect")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GgemuTheme {
        ConnectionLauncher()
    }
}