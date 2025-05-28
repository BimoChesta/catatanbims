package com.bimo0064.catatanbims

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.bimo0064.catatanbims.ui.navigation.AppNavGraph
import com.bimo0064.catatanbims.ui.theme.CatatanBimsTheme
import com.bimo0064.catatanbims.viewmodel.NoteViewModel
import com.bimo0064.catatanbims.viewmodel.NoteViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as NoteApplication
        val factory = NoteViewModelFactory(app.repository)

        setContent {
            // Inisialisasi ThemeViewModel
            val themeViewModel: ThemeViewModel = viewModel()
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

            CatatanBimsTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()
                val noteViewModel: NoteViewModel = viewModel(factory = factory)

                AppNavGraph(
                    navController = navController,
                    viewModel = noteViewModel,
                    themeViewModel = themeViewModel // Kirimkan ThemeViewModel
                )
            }
        }
    }
}
