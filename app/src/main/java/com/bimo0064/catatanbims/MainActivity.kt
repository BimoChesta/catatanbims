package com.bimo0064.catatanbims

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.bimo0064.catatanbims.local.NoteDatabase
import com.bimo0064.catatanbims.repository.NoteRepository
import com.bimo0064.catatanbims.ui.navigation.AppNavGraph
import com.bimo0064.catatanbims.ui.theme.CatatanBimsTheme
import com.bimo0064.catatanbims.viewmodel.NoteViewModel
import com.bimo0064.catatanbims.viewmodel.NoteViewModelFactory
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java,
            "note_database"
        ).build()

        val repository = NoteRepository(database.noteDao())
        val viewModelFactory = NoteViewModelFactory(repository)

        setContent {
            CatatanBimsTheme {
                val navController = rememberNavController()
                val viewModel: NoteViewModel = viewModel(factory = viewModelFactory)

                AppNavGraph(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}
