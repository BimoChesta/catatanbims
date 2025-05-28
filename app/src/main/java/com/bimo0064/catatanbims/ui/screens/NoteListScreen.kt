package com.bimo0064.catatanbims.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bimo0064.catatanbims.R
import com.bimo0064.catatanbims.ThemeViewModel
import com.bimo0064.catatanbims.local.Note
import com.bimo0064.catatanbims.viewmodel.NoteViewModel

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
    themeViewModel: ThemeViewModel
) {
    var isGridView by remember { mutableStateOf(false) }
    val notes by viewModel.notes.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catatan") },
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
            FloatingActionButton(onClick = onAddNoteClick) {
                Text("+")
            }
        }
    ) { padding ->
        if (isGridView) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                items(notes) { note ->
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
                items(notes) { note ->
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
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.clickable { onClick() }
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = note.content)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                if (note.isTrashed) {
                    TextButton(onClick = onRestore) {
                        Text("Pulihkan")
                    }
                    TextButton(onClick = onPermanentDelete) {
                        Text("Hapus Permanen")
                    }
                } else {
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
}