package com.bimo0064.catatanbims.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bimo0064.catatanbims.local.Note
import com.bimo0064.catatanbims.viewmodel.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    viewModel: NoteViewModel,
    noteToEdit: Note? = null,
    onSave: () -> Unit
) {
    var title by remember { mutableStateOf(noteToEdit?.title ?: "") }
    var content by remember { mutableStateOf(noteToEdit?.content ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(if (noteToEdit == null) "Tambah Catatan" else "Edit Catatan")
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val note = Note(
                    id = noteToEdit?.id ?: 0,
                    title = title,
                    content = content,
                    isArchived = noteToEdit?.isArchived ?: false,
                    isTrashed = false
                )
                if (noteToEdit == null) {
                    viewModel.addNote(note)
                } else {
                    viewModel.updateNote(note)
                }
                onSave()
            }) {
                Text("✔")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Judul") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Isi Catatan") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
    }
}