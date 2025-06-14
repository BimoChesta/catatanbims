package com.bimo0064.catatanbims.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
    onAboutClick: () -> Unit // Tambahkan ini
) {
    var isGridView by remember { mutableStateOf(false) }
    var selectedScreen by remember { mutableStateOf("notes") } // default harusnya notes
    var searchQuery by remember { mutableStateOf("") }
    var isDarkMode by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val notes = when (selectedScreen) {
        "archived" -> viewModel.archivedNotes.collectAsState(initial = emptyList())
        "trashed" -> viewModel.trashedNotes.collectAsState(initial = emptyList())
        else -> viewModel.notes.collectAsState(initial = emptyList())
    }

    val filteredNotes = notes.value.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.content.contains(searchQuery, ignoreCase = true)
    }

    MaterialTheme(
        colorScheme = if (isDarkMode) darkColorScheme() else lightColorScheme()
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Text(
                        "Menu",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Dark Mode")
                        Spacer(Modifier.weight(1f))
                        Switch(checked = isDarkMode, onCheckedChange = { isDarkMode = it })
                    }
                }
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = { Text("Cari catatan...") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
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
                                        if (isGridView) R.string.list else R.string.grid
                                    ),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            IconButton(onClick = onAboutClick) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_info_outline_24),
                                    contentDescription = "Tentang"
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
                if (filteredNotes.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
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
                            items(filteredNotes) { note ->
                                NoteItem(
                                    note = note,
                                    onClick = { onNoteClick(note) },
                                    onDelete = { onDelete(note) },
                                    onArchive = { onArchive(note) },
                                    onRestore = { onRestore(note) },
                                    onPermanentDelete = { onPermanentDelete(note) },
                                    isTrashScreen = selectedScreen == "trashed",
                                    isArchivedScreen = selectedScreen == "archived"
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .padding(padding)
                                .fillMaxSize()
                        ) {
                            items(filteredNotes) { note ->
                                NoteItem(
                                    note = note,
                                    onClick = { onNoteClick(note) },
                                    onDelete = { onDelete(note) },
                                    onArchive = { onArchive(note) },
                                    onRestore = { onRestore(note) },
                                    onPermanentDelete = { onPermanentDelete(note) },
                                    isTrashScreen = selectedScreen == "trashed",
                                    isArchivedScreen = selectedScreen == "archived"
                                )
                            }
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
    onPermanentDelete: () -> Unit,
    isTrashScreen: Boolean = false,
    isArchivedScreen: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isTrashScreen) { onClick() } // Nonaktifkan edit kalau di Trash
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = note.title)
            Text(text = note.content)
            Text("Prioritas: ${note.priority}", style = MaterialTheme.typography.labelSmall)

            Row {
                if (isTrashScreen) {
                    TextButton(onClick = onRestore) {
                        Text("Pulihkan")
                    }
                    TextButton(onClick = onPermanentDelete) {
                        Text("Hapus Permanen")
                    }
                } else if (isArchivedScreen) {
                    TextButton(onClick = onRestore) {
                        Text("Kembalikan")
                    }
                    TextButton(onClick = onDelete) {
                        Text("Buang")
                    }
                } else {
                    TextButton(onClick = onArchive) {
                        Text("Arsipkan")
                    }
                    TextButton(onClick = onDelete) {
                        Text("Buang kesampah")
                    }
                }
            }
        }
    }
}