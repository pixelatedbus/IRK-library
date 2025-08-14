package com.irk.test.ui.theme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.irk.test.viewmodel.CryptoViewModel
import com.irk.test.viewmodel.CipherType
import com.irk.test.viewmodel.CryptoUiState
import com.irk.test.viewmodel.StepUiModel

@Composable
fun CryptoScreen(
    modifier: Modifier = Modifier,
    viewModel: CryptoViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ControlsSection(uiState = uiState, viewModel = viewModel)

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        ResultsSection(uiState = uiState)
    }
}

@Composable
private fun ControlsSection(uiState: CryptoUiState, viewModel: CryptoViewModel) {
    OutlinedTextField(
        value = uiState.inputText,
        onValueChange = { viewModel.onInputTextChanged(it) },
        label = { if (uiState.isEncryptMode) Text("Plaintext") else Text("Ciphertext") },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("Cipher:", modifier = Modifier.padding(end = 16.dp))
        SelectableButton(
            text = "Caesar",
            isSelected = uiState.selectedCipher == CipherType.CAESAR,
            onClick = { viewModel.onCipherSelected(CipherType.CAESAR) }
        )
        Spacer(modifier = Modifier.width(8.dp))
        SelectableButton(
            text = "RSA",
            isSelected = uiState.selectedCipher == CipherType.RSA,
            onClick = { viewModel.onCipherSelected(CipherType.RSA) }
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    if (uiState.selectedCipher == CipherType.CAESAR) {
        OutlinedTextField(
            value = uiState.caesarShift,
            onValueChange = { viewModel.onCaesarShiftChanged(it) },
            label = { Text("Shift") },
            modifier = Modifier.fillMaxWidth()
        )
    } else { // RSA
        if (uiState.isEncryptMode) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = uiState.rsaP,
                    onValueChange = { viewModel.onRsaKeyParamsChanged(p = it) },
                    label = { Text("Prime p") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = uiState.rsaQ,
                    onValueChange = { viewModel.onRsaKeyParamsChanged(q = it) },
                    label = { Text("Prime q") },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { viewModel.generateRsaKeys() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Generate RSA Keys")
            }
        } else {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = uiState.rsaN,
                    onValueChange = { viewModel.onRsaDecryptKeyChanged(n = it) },
                    label = { Text("Modulus n") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = uiState.rsaD,
                    onValueChange = { viewModel.onRsaDecryptKeyChanged(d = it) },
                    label = { Text("Private Key d") },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Row(verticalAlignment = Alignment.CenterVertically) {
        SelectableButton(
            text = "Encrypt",
            isSelected = uiState.isEncryptMode,
            onClick = { viewModel.onModeToggled(true) }
        )
        Spacer(modifier = Modifier.width(8.dp))
        SelectableButton(
            text = "Decrypt",
            isSelected = !uiState.isEncryptMode,
            onClick = { viewModel.onModeToggled(false) }
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { viewModel.process() }, modifier = Modifier.height(48.dp)) {
            Text("Process")
        }
    }
}

@Composable
private fun ColumnScope.ResultsSection(uiState: CryptoUiState) {
    LazyColumn(modifier = Modifier.weight(1f)) {
        item {
            Text("Result: ${uiState.outputText}", fontWeight = FontWeight.Bold)
            if (uiState.error != null) {
                Text("Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (uiState.steps.isNotEmpty()) {
            item {
                Text("Steps:", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 8.dp))
            }
            items(uiState.steps) { step ->
                StepItem(step = step)
            }
        }
    }
}


@Composable
fun StepItem(step: StepUiModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = step.title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = step.detail,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun SelectableButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = if (isSelected) ButtonDefaults.buttonColors() else ButtonDefaults.outlinedButtonColors(),
        border = if(isSelected) null else ButtonDefaults.outlinedButtonBorder
    ) {
        Text(text)
    }
}
