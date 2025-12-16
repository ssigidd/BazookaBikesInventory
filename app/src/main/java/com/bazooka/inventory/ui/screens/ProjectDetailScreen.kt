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
import com.bazooka.inventory.ui.viewmodel.ProjectViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    projectId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Long) -> Unit,
    viewModel: ProjectViewModel = hiltViewModel()
) {
    val projectDetail by viewModel.getProjectWithParts(projectId).collectAsState(
        initial = com.bazooka.inventory.ui.viewmodel.ProjectDetailUiState(isLoading = true)
    )
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showMenuDropdown by remember { mutableStateOf(false) }
    var showArchiveDialog by remember { mutableStateOf(false) }
    var partToRemove by remember { mutableStateOf<Triple<Long, String, Int>?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Project Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    projectDetail.project?.let { project ->
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
                                        onNavigateToEdit(projectId)
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.Edit, "Edit")
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(if (project.isArchived) "Unarchive" else "Archive") },
                                    onClick = {
                                        showMenuDropdown = false
                                        showArchiveDialog = true
                                    },
                                    leadingIcon = {
                                        Icon(
                                            if (project.isArchived) Icons.Default.Unarchive else Icons.Default.Archive,
                                            if (project.isArchived) "Unarchive" else "Archive"
                                        )
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
        projectDetail.project?.let { project ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = project.name,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            if (project.isArchived) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = MaterialTheme.shapes.small
                                ) {
                                    Text(
                                        text = "Archived",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    if (project.description.isNotBlank()) {
                        DetailSection(
                            label = "Description",
                            content = project.description
                        )
                    }
                }

                item {
                    project.budget?.let {
                        DetailRow(
                            label = "Budget",
                            value = "$${String.format("%.2f", it)}"
                        )
                    }
                }

                item {
                    project.targetCompletionDate?.let {
                        DetailRow(
                            label = "Target Completion",
                            value = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                                .format(Date(it))
                        )
                    }
                }

                item {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(
                        text = "Associated Parts (${projectDetail.bikeParts.size})",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (projectDetail.bikeParts.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No parts associated with this project",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    items(projectDetail.bikeParts, key = { it.bikePart.id }) { partWithDetails ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = partWithDetails.bikePart.name,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = partWithDetails.bikePart.category,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(
                                                text = "$${String.format("%.2f", partWithDetails.bikePart.price)}",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "Qty: ${partWithDetails.quantityInProject}",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                }
                                IconButton(
                                    onClick = {
                                        partToRemove = Triple(
                                            partWithDetails.bikePart.id,
                                            partWithDetails.bikePart.name,
                                            partWithDetails.quantityInProject
                                        )
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Remove part from project",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }

                    item {
                        val totalCost = projectDetail.bikeParts.sumOf {
                            it.bikePart.price * it.quantityInProject
                        }
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Total Parts Cost",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "$${String.format("%.2f", totalCost)}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        } ?: Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Project") },
            text = { Text("Are you sure you want to delete this project? All associated parts will be unlinked.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        projectDetail.project?.let { viewModel.deleteProject(it) }
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

    if (showArchiveDialog) {
        AlertDialog(
            onDismissRequest = { showArchiveDialog = false },
            title = { Text(if (projectDetail.project?.isArchived == true) "Unarchive Project" else "Archive Project") },
            text = {
                Text(
                    if (projectDetail.project?.isArchived == true)
                        "Are you sure you want to unarchive this project?"
                    else
                        "Are you sure you want to archive this project? It will be moved to the archived projects list."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        projectDetail.project?.let { project ->
                            if (project.isArchived) {
                                viewModel.unarchiveProject(project)
                            } else {
                                viewModel.archiveProject(project)
                            }
                        }
                        showArchiveDialog = false
                        onNavigateBack()
                    }
                ) {
                    Text(if (projectDetail.project?.isArchived == true) "Unarchive" else "Archive")
                }
            },
            dismissButton = {
                TextButton(onClick = { showArchiveDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    partToRemove?.let { (bikePartId, partName, maxQuantity) ->
        var quantityToRemove by remember { mutableStateOf(maxQuantity.toString()) }

        AlertDialog(
            onDismissRequest = { partToRemove = null },
            title = { Text("Remove Part from Project") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Remove \"$partName\" from this project. This will not add the part back to stock.")

                    OutlinedTextField(
                        value = quantityToRemove,
                        onValueChange = { newValue ->
                            if (newValue.isEmpty() || (newValue.all { it.isDigit() } && newValue.toIntOrNull()?.let { it <= maxQuantity } == true)) {
                                quantityToRemove = newValue
                            }
                        },
                        label = { Text("Quantity to remove") },
                        supportingText = { Text("Max: $maxQuantity") },
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val qty = quantityToRemove.toIntOrNull()
                        if (qty != null && qty > 0 && qty <= maxQuantity) {
                            viewModel.removePartFromProject(projectId, bikePartId, qty)
                            partToRemove = null
                        }
                    },
                    enabled = quantityToRemove.toIntOrNull()?.let { it > 0 && it <= maxQuantity } == true
                ) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(onClick = { partToRemove = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}
