package com.irk.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.irk.test.ui.theme.AboutScreen
import com.irk.test.ui.theme.CryptoScreen
import com.irk.test.ui.theme.HuffmanScreen
import com.irk.test.ui.theme.MatrixScreen
import com.irk.test.ui.theme.ReferenceScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var selectedScreen by remember { mutableStateOf(0) }

            Scaffold(
                bottomBar = {
                    NavigationBar {
                        NavigationBarItem(
                            selected = selectedScreen == 0,
                            onClick = { selectedScreen = 0 },
                            label = { Text("Matrix") },
                            icon = {}
                        )
                        NavigationBarItem(
                            selected = selectedScreen == 1,
                            onClick = { selectedScreen = 1 },
                            label = { Text("Crypto") },
                            icon = {}
                        )
                        NavigationBarItem(
                            selected = selectedScreen == 2,
                            onClick = { selectedScreen = 2 },
                            label = { Text("Huffman") },
                            icon = {}
                        )
                        NavigationBarItem(
                            selectedScreen == 3,
                            onClick = { selectedScreen = 3 },
                            label = { Text("References")},
                            icon = {}
                        )
                        NavigationBarItem(
                            selected = selectedScreen == 4,
                            onClick = { selectedScreen = 4 },
                            label = { Text("About") },
                            icon = {}
                        )
                    }
                }
            ) { padding ->
                when (selectedScreen) {
                    0 -> MatrixScreen()
                    1 -> CryptoScreen(modifier = Modifier.padding(padding))
                    2 -> HuffmanScreen(modifier = Modifier.padding(padding))
                    3 -> ReferenceScreen()
                    4 -> AboutScreen()
                }
            }
        }
    }
}