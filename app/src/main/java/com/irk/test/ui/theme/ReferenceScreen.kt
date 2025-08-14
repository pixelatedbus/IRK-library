package com.irk.test.ui.theme

import android.content.Intent
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import com.irk.test.model.ReferenceItem
import com.irk.test.viewmodel.MaterialFilter
import com.irk.test.viewmodel.ReferenceViewModel

@Composable
fun ReferenceScreen(viewModel: ReferenceViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    ReferenceListScreen(
        uiState = uiState,
        viewModel = viewModel,
        onItemSelected = { referenceItem ->
            val intent = Intent(Intent.ACTION_VIEW, referenceItem.url.toUri())
            context.startActivity(intent)
        }
    )
}

@Composable
fun ReferenceListScreen(
    uiState: com.irk.test.viewmodel.ReferenceUiState,
    viewModel: ReferenceViewModel,
    onItemSelected: (ReferenceItem) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = viewModel::onSearchQueryChanged,
            label = { Text("Apa yang mau dipelajari sekarang?") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        FilterChips(
            selectedFilter = uiState.selectedFilter,
            onFilterSelected = viewModel::onFilterChanged
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(uiState.filteredReferences) { item ->
                ReferenceListItem(item = item, onClick = { onItemSelected(item) })
            }
        }
    }
}

@Composable
fun FilterChips(selectedFilter: MaterialFilter, onFilterSelected: (MaterialFilter) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        MaterialFilter.values().forEach { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = { Text(filter.name) }
            )
        }
    }
}

@Composable
fun ReferenceListItem(item: ReferenceItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = item.title,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}