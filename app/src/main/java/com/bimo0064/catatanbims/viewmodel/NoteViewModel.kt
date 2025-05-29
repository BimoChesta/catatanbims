package com.bimo0064.catatanbims.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bimo0064.catatanbims.local.Note
import com.bimo0064.catatanbims.repository.NoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {
    val notes = repository.allNotes
        .map { it.sortedByDescending { note -> note.id } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val archivedNotes = repository.archivedNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val trashedNotes = repository.trashedNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addNote(note: Note) = viewModelScope.launch {
        repository.insertNote(note)
    }

    fun updateNote(note: Note) = viewModelScope.launch {
        repository.updateNote(note)
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.updateNote(note.copy(isTrashed = true))
        }
    }

    fun archiveNote(note: Note) {
        viewModelScope.launch {
            repository.updateNote(note.copy(isArchived = true))
        }
    }

    fun restoreNote(note: Note) {
        viewModelScope.launch {
            repository.updateNote(note.copy(isArchived = false, isTrashed = false))
        }
    }

    fun permanentlyDeleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }
}