package com.bazooka.inventory.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bazooka.inventory.data.local.entity.BikePart
import com.bazooka.inventory.ui.viewmodel.BikePartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBikePartScreen(
    onNavigateBack: () -> Unit,
    onSaved: () -> Unit,
    viewModel: BikePartViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var minimalStock by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var manufacturer by remember { mutableStateOf("") }
    var serialNumber by remember { mutableStateOf("") }
    var showCategoryDropdown by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    val categories = viewModel.getCategories()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Bike Part") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (name.isBlank() || selectedCategory.isBlank()) {
                                showError = true
                            } else {
                                val bikePart = BikePart(
                                    name = name.trim(),
                                    description = description.trim(),
                                    category = selectedCategory,
                                    quantity = quantity.toIntOrNull() ?: 0,
                                    minimalStock = minimalStock.toIntOrNull() ?: 0,
                                    price = price.toDoubleOrNull() ?: 0.0,
                                    manufacturer = manufacturer.trim().ifBlank { null },
                                    serialNumber = serialNumber.trim().ifBlank { null }
                                )
                                viewModel.insertBikePart(bikePart)
                                onSaved()
                            }
                        }
                    ) {
                        Icon(Icons.Default.Save, "Save")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name *") },
                modifier = Modifier.fillMaxWidth(),
                isError = showError && name.isBlank(),
                supportingText = {
                    if (showError && name.isBlank()) {
                        Text("Name is required")
                    }
                },
                singleLine = true
            )

            ExposedDropdownMenuBox(
                expanded = showCategoryDropdown,
                onExpandedChange = { showCategoryDropdown = it }
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCategoryDropdown) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    isError = showError && selectedCategory.isBlank(),
                    supportingText = {
                        if (showError && selectedCategory.isBlank()) {
                            Text("Category is required")
                        }
                    }
                )
                ExposedDropdownMenu(
                    expanded = showCategoryDropdown,
                    onDismissRequest = { showCategoryDropdown = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                selectedCategory = category
                                showCategoryDropdown = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it.filter { char -> char.isDigit() } },
                    label = { Text("Quantity") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                OutlinedTextField(
                    value = minimalStock,
                    onValueChange = { minimalStock = it.filter { char -> char.isDigit() } },
                    label = { Text("Min. Stock") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }

            OutlinedTextField(
                value = price,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
                        price = newValue
                    }
                },
                label = { Text("Price") },
                leadingIcon = { Text("$") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )

            OutlinedTextField(
                value = manufacturer,
                onValueChange = { manufacturer = it },
                label = { Text("Manufacturer") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = serialNumber,
                onValueChange = { serialNumber = it },
                label = { Text("Serial Number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            if (showError) {
                Text(
                    text = "* Required fields",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
