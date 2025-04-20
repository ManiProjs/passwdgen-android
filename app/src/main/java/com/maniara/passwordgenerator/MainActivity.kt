package com.maniarasteh.passwordgenerator

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PasswordGeneratorApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordGeneratorApp() {
    var length by remember { mutableFloatStateOf(12f) }
    var includeUpper by remember { mutableStateOf(true) }
    var includeLower by remember { mutableStateOf(true) }
    var includeDigits by remember { mutableStateOf(true) }
    var includeSymbols by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    fun generatePassword(): String {
        val upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val lower = "abcdefghijklmnopqrstuvwxyz"
        val digits = "0123456789"
        val symbols = "!@#$%^&*()_+-=[]{}|;:,.<>?/"
        var charPool = ""
        if (includeUpper) charPool += upper
        if (includeLower) charPool += lower
        if (includeDigits) charPool += digits
        if (includeSymbols) charPool += symbols

        if (charPool.isEmpty()) return ""

        return (1..length.toInt())
            .map { charPool.random() }
            .joinToString("")
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Password Generator") })
        }
    ) { innerPadding -> // innerPadding is the PaddingValues
        Column(
            Modifier
                .padding(innerPadding) // Use the provided padding here
                .padding(16.dp) // you can add your padding
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Password Length: ${length.toInt()}")
            Slider(
                value = length,
                onValueChange = { length = it },
                valueRange = 4f..128f,
                steps = 28
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = includeUpper, onCheckedChange = { includeUpper = it })
                Text("Include Uppercase")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = includeLower, onCheckedChange = { includeLower = it })
                Text("Include Lowercase")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = includeDigits, onCheckedChange = { includeDigits = it })
                Text("Include Digits")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = includeSymbols, onCheckedChange = { includeSymbols = it })
                Text("Include Symbols")
            }

            Button(
                onClick = {
                    password = generatePassword()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Generate Password")
            }

            if (password.isNotEmpty()) {
                Text("Generated Password:", style = MaterialTheme.typography.bodyMedium)
                Text(password, style = MaterialTheme.typography.bodyMedium)
                Button(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(password))
                        Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Copy")
                }
            }
        }
    }
}
