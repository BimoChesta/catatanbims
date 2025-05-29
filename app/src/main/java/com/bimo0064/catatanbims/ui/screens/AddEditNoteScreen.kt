package com.bimo0064.catatanbims.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bimo0064.catatanbims.local.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    note: Note?,
    onSave: (Note) -> Unit,
    onCancel: () -> Unit
) {
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    var priority by remember { mutableStateOf(note?.priority ?: "Tidak Terlalu Penting") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (note == null) "Tambah Catatan" else "Edit Catatan") })
        }
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Judul") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Isi") },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )

            Text("Prioritas", modifier = Modifier.padding(top = 8.dp))
            val priorities = listOf("Tidak Terlalu Penting", "Penting", "Sangat Penting")
            priorities.forEach {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = priority == it,
                        onClick = { priority = it }
                    )
                    Text(text = it)
                }
            }

            Row(modifier = Modifier.padding(top = 16.dp)) {
                TextButton(onClick = onCancel) {
                    Text("Batal")
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = {
                    if (title.isNotBlank()) {
                        onSave(Note(id = note?.id ?: 0, title = title, content = content, priority = priority, isArchived = note?.isArchived ?: false, isTrashed = note?.isTrashed ?: false))
                    }
                }) {
                    Text("Simpan")
                }
            }
        }
    }
}
