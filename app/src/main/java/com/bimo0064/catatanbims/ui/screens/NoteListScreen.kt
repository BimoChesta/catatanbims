package com.bimo0064.catatanbims.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bimo0064.catatanbims.R
import com.bimo0064.catatanbims.local.Note
import com.bimo0064.catatanbims.viewmodel.NoteViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    viewModel: NoteViewModel,
    onNoteClick: (Note) -> Unit,
    onAddNoteClick: () -> Unit,
    onDelete: (Note) -> Unit,
    onArchive: (Note) -> Unit,
    onRestore: (Note) -> Unit,
    onPermanentDelete: (Note) -> Unit,
) {
    var isGridView by remember { mutableStateOf(false) }
    var selectedScreen by remember { mutableStateOf("notes") }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val notes = when (selectedScreen) {
        "archived" -> viewModel.archivedNotes.collectAsState(initial = emptyList())
        "trashed" -> viewModel.trashedNotes.collectAsState(initial = emptyList())
        else -> viewModel.notes.collectAsState(initial = emptyList())
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menu", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
                NavigationDrawerItem(
                    label = { Text("Catatan") },
                    selected = selectedScreen == "notes",
                    onClick = {
                        selectedScreen = "notes"
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Arsip") },
                    selected = selectedScreen == "archived",
                    onClick = {
                        selectedScreen = "archived"
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Sampah") },
                    selected = selectedScreen == "trashed",
                    onClick = {
                        selectedScreen = "trashed"
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Catatan") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = { isGridView = !isGridView }) {
                            Icon(
                                painter = painterResource(
                                    if (isGridView) R.drawable.baseline_view_list_24
                                    else R.drawable.baseline_grid_view_24
                                ),
                                contentDescription = stringResource(
                                    if (isGridView) R.string.list
                                    else R.string.grid
                                ),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                if (selectedScreen == "notes") {
                    FloatingActionButton(onClick = onAddNoteClick) {
                        Text("+")
                    }
                }
            }
        ) { padding ->
            if (notes.value.isEmpty()) {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text("Tidak ada catatan")
                }
            } else {
                if (isGridView) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                    ) {
                        items(notes.value) { note ->
                            NoteItem(
                                note = note,
                                onClick = { onNoteClick(note) },
                                onDelete = { onDelete(note) },
                                onArchive = { onArchive(note) },
                                onRestore = { onRestore(note) },
                                onPermanentDelete = { onPermanentDelete(note) }
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                    ) {
                        items(notes.value) { note ->
                            NoteItem(
                                note = note,
                                onClick = { onNoteClick(note) },
                                onDelete = { onDelete(note) },
                                onArchive = { onArchive(note) },
                                onRestore = { onRestore(note) },
                                onPermanentDelete = { onPermanentDelete(note) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onArchive: () -> Unit,
    onRestore: () -> Unit,
    onPermanentDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.clickable { onClick() }
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = note.content)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                TextButton(onClick = onClick) {
                    Text("Edit")
                }
                TextButton(onClick = onDelete) {
                    Text("Hapus")
                }
                TextButton(onClick = onArchive) {
                    Text("Arsip")
                }
            }
        }
    }
}