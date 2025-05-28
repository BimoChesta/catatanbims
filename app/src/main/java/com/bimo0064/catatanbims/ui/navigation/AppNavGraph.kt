package com.bimo0064.catatanbims.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bimo0064.catatanbims.ui.screens.NoteListScreen
import com.bimo0064.catatanbims.viewmodel.NoteViewModel

sealed class Screen(val route: String) {
    object Notes : Screen("notes")
    // Nanti kita tambahkan AddEditNote
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: NoteViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Notes.route,
        modifier = modifier
    ) {
        composable(Screen.Notes.route) {
            NoteListScreen(
                viewModel = viewModel,
                onNoteClick = { /* nanti ke detail atau edit */ },
                onAddNoteClick = { /* nanti ke tambah */ }
            )
        }
    }
}