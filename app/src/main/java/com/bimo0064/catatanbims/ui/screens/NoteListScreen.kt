package com.bimo0064.catatanbims.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bimo0064.catatanbims.local.Note
import com.bimo0064.catatanbims.viewmodel.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    viewModel: NoteViewModel,
    onNoteClick: (Note) -> Unit,
    onAddNoteClick: () -> Unit,
    onDelete: (Note) -> Unit,
    onArchive: (Note) -> Unit
) {
    val notes = viewModel.notes.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Catatan") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNoteClick) {
                Text("+")
            }
        }
    ) { padding ->
        if (notes.value.isEmpty()) {
            Box(modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("Belum ada catatan")
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
                        onArchive = { onArchive(note) }
                    )
                }

            }
            }
        }
    }


@Composable
fun NoteItem(note: Note, onClick: () -> Unit, onDelete: () -> Unit, onArchive: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier
            .padding(16.dp)
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
