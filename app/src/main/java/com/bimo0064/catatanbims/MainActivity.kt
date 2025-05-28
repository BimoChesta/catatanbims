package com.bimo0064.catatanbims

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.bimo0064.catatanbims.repository.NoteRepository
import com.bimo0064.catatanbims.ui.navigation.AppNavGraph
import com.bimo0064.catatanbims.ui.theme.CatatanBimsTheme
import com.bimo0064.catatanbims.viewmodel.NoteViewModel
import com.bimo0064.catatanbims.viewmodel.NoteViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = NoteDatabase.getDatabase(this).noteDao()
        val repository = NoteRepository(dao)
        val factory = NoteViewModelFactory(repository)

        setContent {
            CatatanBimsTheme {
                val viewModel: NoteViewModel = viewModel(factory = factory)
                val navController = rememberNavController()
                AppNavGraph(navController = navController, viewModel = viewModel)
            }
        }
    }
}