package com.bazooka.inventory.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bazooka.inventory.data.local.entity.BikePart
import com.bazooka.inventory.ui.viewmodel.BikePartViewModel
import com.bazooka.inventory.ui.viewmodel.ProjectViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BikePartDetailScreen(
    bikePartId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Long) -> Unit,
    viewModel: BikePartViewModel = hiltViewModel(),
    projectViewModel: ProjectViewModel = hiltViewModel()
) {
    val bikePart by viewModel.getBikePartById(bikePartId).collectAsState(initial = null)
    val projectsState by projectViewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showMenuDropdown by remember { mutableStateOf(false) }
    var showRemoveDialog by remember { mutableStateOf(false) }
    var showAddQuantityDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Part Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    bikePart?.let {
                        Box {
                            IconButton(onClick = { showMenuDropdown = true }) {
                                Icon(Icons.Default.MoreVert, "More options")
                            }
                            DropdownMenu(
                                expanded = showMenuDropdown,
                                onDismissRequest = { showMenuDropdown = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Edit") },
                                    onClick = {
                                        showMenuDropdown = false
                                        onNavigateToEdit(bikePartId)
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.Edit, "Edit")
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Delete") },
                                    onClick = {
                                        showMenuDropdown = false
                                        showDeleteDialog = true
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.Delete, "Delete")
                                    }
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        bikePart?.let { part ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = part.name,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = part.category,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    DetailRow(label = "Price", value = "$${String.format("%.2f", part.price)}")
                    DetailRow(label = "Quantity", value = part.quantity.toString())

                    if (part.description.isNotBlank()) {
                        DetailSection(
                            label = "Description",
                            content = part.description
                        )
                    }

                    part.manufacturer?.let {
                        DetailRow(label = "Manufacturer", value = it)
                    }

                    part.serialNumber?.let {
                        DetailRow(label = "Serial Number", value = it)
                    }

                    DetailRow(
                        label = "Date Added",
                        value = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                            .format(Date(part.dateAdded))
                    )
                }

                // Bottom buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Small Add button
                    OutlinedButton(
                        onClick = { showAddQuantityDialog = true },
                        modifier = Modifier.weight(0.3f)
                    ) {
                        Icon(Icons.Default.Add, "Add quantity")
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    // Large Remove button
                    Button(
                        onClick = { showRemoveDialog = true },
                        modifier = Modifier.weight(0.7f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        enabled = part.quantity > 0
                    ) {
                        Icon(Icons.Default.Remove, "Remove from stock")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Remove from Stock")
                    }
                }
            }
        } ?: Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Part") },
            text = { Text("Are you sure you want to delete this bike part?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        bikePart?.let { viewModel.deleteBikePart(it) }
                        showDeleteDialog = false
                        onNavigateBack()
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showAddQuantityDialog) {
        var quantityToAdd by remember { mutableStateOf("1") }
        AlertDialog(
            onDismissRequest = { showAddQuantityDialog = false },
            title = { Text("Add to Stock") },
            text = {
                Column {
                    Text("How many units do you want to add?")
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = quantityToAdd,
                        onValueChange = { quantityToAdd = it.filter { char -> char.isDigit() } },
                        label = { Text("Quantity") },
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                        ),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        bikePart?.let { part ->
                            val addAmount = quantityToAdd.toIntOrNull() ?: 0
                            if (addAmount > 0) {
                                val updatedPart = part.copy(quantity = part.quantity + addAmount)
                                viewModel.updateBikePart(updatedPart)
                            }
                        }
                        showAddQuantityDialog = false
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddQuantityDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showRemoveDialog) {
        var quantityToRemove by remember { mutableStateOf("1") }
        var selectedProjectId by remember { mutableStateOf<Long?>(null) }
        var showProjectDropdown by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showRemoveDialog = false },
            title = { Text("Remove from Stock") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Enter quantity to remove and optionally assign to a project:")

                    OutlinedTextField(
                        value = quantityToRemove,
                        onValueChange = { quantityToRemove = it.filter { char -> char.isDigit() } },
                        label = { Text("Quantity") },
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    ExposedDropdownMenuBox(
                        expanded = showProjectDropdown,
                        onExpandedChange = { showProjectDropdown = it }
                    ) {
                        OutlinedTextField(
                            value = selectedProjectId?.let { id ->
                                projectsState.projects.find { it.id == id }?.name ?: "No project"
                            } ?: "No project",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Project (optional)") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = showProjectDropdown)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = showProjectDropdown,
                            onDismissRequest = { showProjectDropdown = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("No project") },
                                onClick = {
                                    selectedProjectId = null
                                    showProjectDropdown = false
                                }
                            )
                            projectsState.projects.forEach { project ->
                                DropdownMenuItem(
                                    text = { Text(project.name) },
                                    onClick = {
                                        selectedProjectId = project.id
                                        showProjectDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        bikePart?.let { part ->
                            val removeAmount = quantityToRemove.toIntOrNull() ?: 0
                            if (removeAmount > 0 && removeAmount <= part.quantity) {
                                // Reduce stock quantity
                                val updatedPart = part.copy(quantity = part.quantity - removeAmount)
                                viewModel.updateBikePart(updatedPart)

                                // Add to project if selected
                                selectedProjectId?.let { projectId ->
                                    projectViewModel.addPartToProject(projectId, part.id, removeAmount)
                                }
                            }
                        }
                        showRemoveDialog = false
                    },
                    enabled = quantityToRemove.toIntOrNull()?.let { it > 0 && it <= (bikePart?.quantity ?: 0) } == true
                ) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DetailSection(
    label: String,
    content: String
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
