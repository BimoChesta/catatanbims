package com.bimo0064.catatanbims

import android.app.Application
import com.bimo0064.catatanbims.local.NoteDatabase
import com.bimo0064.catatanbims.repository.NoteRepository

class NoteApplication : Application() {
    lateinit var repository: NoteRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val database = NoteDatabase.getDatabase(this)
        repository = NoteRepository(database.noteDao())
    }
}
