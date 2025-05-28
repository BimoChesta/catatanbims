package com.bimo0064.catatanbims.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note WHERE isArchived = 0 AND isTrashed = 0")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM Note WHERE isArchived = 1 AND isTrashed = 0")
    fun getArchivedNotes(): Flow<List<Note>>

    @Query("SELECT * FROM Note WHERE isTrashed = 1")
    fun getTrashedNotes(): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}
