package com.irk.test.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irk.test.model.Crypto
import com.irk.test.model.CryptoStep
import com.irk.test.model.RSAKeys
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigInteger

enum class CipherType {
    CAESAR,
    RSA
}

data class StepUiModel(val title: String, val detail: String)
data class RSAKeysUiModel(val n: String, val e: String, val d: String)

data class CryptoUiState(
    val inputText: String = "",
    val outputText: String = "",
    val selectedCipher: CipherType = CipherType.CAESAR,
    val caesarShift: String = "3",
    val rsaP: String = "",
    val rsaQ: String = "",
    val rsaN: String = "",
    val rsaD: String = "",
    val rsaKeys: RSAKeysUiModel? = null,
    val isEncryptMode: Boolean = true,
    val steps: List<StepUiModel> = emptyList(),
    val error: String? = null
)

class CryptoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CryptoUiState())
    val uiState: StateFlow<CryptoUiState> = _uiState.asStateFlow()

    // --- Event Handlers ---

    fun onInputTextChanged(text: String) {
        _uiState.update { it.copy(inputText = text, error = null) }
    }

    fun onCipherSelected(cipherType: CipherType) {
        _uiState.update { it.copy(selectedCipher = cipherType, error = null, outputText = "", steps = emptyList()) }
    }

    fun onCaesarShiftChanged(shift: String) {
        _uiState.update { it.copy(caesarShift = shift, error = null) }
    }

    fun onRsaKeyParamsChanged(p: String? = null, q: String? = null) {
        _uiState.update {
            it.copy(
                rsaP = p ?: it.rsaP,
                rsaQ = q ?: it.rsaQ,
                error = null,
                rsaKeys = null
            )
        }
    }

    fun onRsaDecryptKeyChanged(n: String? = null, d: String? = null) {
        _uiState.update {
            it.copy(
                rsaN = n ?: it.rsaN,
                rsaD = d ?: it.rsaD,
                error = null
            )
        }
    }

    fun onModeToggled(isEncrypt: Boolean) {
        _uiState.update { it.copy(isEncryptMode = isEncrypt, outputText = "", steps = emptyList()) }
    }

    fun generateRsaKeys() {
        viewModelScope.launch {
            val currentState = _uiState.value
            try {
                val p = BigInteger(currentState.rsaP)
                val q = BigInteger(currentState.rsaQ)
                val (modelKeys, modelSteps) = Crypto.generateRsaKeys(p, q)

                val uiKeys = modelKeys.toUiModel()
                val uiSteps = modelSteps.map { it.toUiModel() }

                _uiState.update {
                    it.copy(rsaKeys = uiKeys, steps = uiSteps, error = null, outputText = "Keys Generated Successfully")
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Invalid P or Q. Please use prime numbers.", steps = emptyList()) }
            }
        }
    }

    fun process() {
        viewModelScope.launch {
            val currentState = _uiState.value
            try {
                val (result, modelSteps) = when (currentState.selectedCipher) {
                    CipherType.CAESAR -> processCaesar(currentState)
                    CipherType.RSA -> processRsa(currentState)
                }
                val uiSteps = modelSteps.map { it.toUiModel() }
                _uiState.update { it.copy(outputText = result, steps = uiSteps, error = null) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "An unknown error occurred.", steps = emptyList()) }
            }
        }
    }

    private fun processCaesar(state: CryptoUiState): Pair<String, List<CryptoStep>> {
        val shift = state.caesarShift.toIntOrNull()
            ?: throw IllegalArgumentException("Caesar shift must be a valid integer.")
        val effectiveShift = if (state.isEncryptMode) shift else -shift
        return Crypto.caesarEncrypt(state.inputText, effectiveShift)
    }

    private fun processRsa(state: CryptoUiState): Pair<String, List<CryptoStep>> {
        val keys = if (state.isEncryptMode) {
            state.rsaKeys?.toModel() ?: throw IllegalStateException("RSA keys must be generated first.")
        } else {
            val n = BigInteger(state.rsaN)
            val d = BigInteger(state.rsaD)
            RSAKeys(n = n, e = BigInteger.ZERO, d = d)
        }

        return if (state.isEncryptMode) {
            Crypto.rsaEncrypt(state.inputText, keys)
        } else {
            Crypto.rsaDecrypt(state.inputText, keys)
        }
    }


    private fun CryptoStep.toUiModel(): StepUiModel {
        return StepUiModel(title = this.title, detail = this.description)
    }

    private fun RSAKeys.toUiModel(): RSAKeysUiModel {
        return RSAKeysUiModel(n = this.n.toString(), e = this.e.toString(), d = this.d.toString())
    }

    private fun RSAKeysUiModel.toModel(): RSAKeys {
        return RSAKeys(
            n = BigInteger(this.n),
            e = BigInteger(this.e),
            d = BigInteger(this.d)
        )
    }
}
