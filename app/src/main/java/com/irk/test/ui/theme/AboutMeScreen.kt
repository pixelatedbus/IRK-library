package com.irk.test.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Lutfi Hakim Yusra",
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = "Programmer!",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            InfoCard(
                title = "Motivasi",
                content = "Aplikasi ini dibuat sebagai Swiss Army knife untuk calon asisten IRK, menggabungkan konsep fundamental dari Aljabar Geometri, Matematika Diskrit, dan Strategi Algoritma ke dalam satu tool yang interaktif dan edukatif. Ini merupakan kesempatan berharga bagi saya untuk mempelajari ulang konsep-konsep yang diajari di mata kuliah tersebut."
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            InfoCard(
                title = "Harapan Sebagai Asisten",
                content = "Sebagai asisten, saya berharap dapat membantu mahasiswa memahami materi-materi dasar IRK dengan lebih mudah melalui visualisasi. Saya merupakan orang yang suka mengajar dan memprogram, dan saya percaya menjadi asisten merupakan sarana yang baik untuknya. "
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            InfoCard(
                title = "Kontak",
                content = "Email: luthfihakimyusra@gmail.com\n" +
                        "GitHub: pixelatedbus"
            )
        }
    }
}

@Composable
private fun InfoCard(title: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Justify
            )
        }
    }
}
