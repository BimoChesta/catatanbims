package com.bimo0064.catatanbims.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bimo0064.catatanbims.ui.screens.AddEditNoteScreen
import com.bimo0064.catatanbims.ui.screens.NoteListScreen
import com.bimo0064.catatanbims.viewmodel.NoteViewModel

sealed class Screen(val route: String) {
    object Notes : Screen("notes")
    object AddEdit : Screen("add_edit")
    object AddEditWithArgs : Screen("add_edit/{noteId}") {
        fun createRoute(noteId: Int) = "add_edit/$noteId"
    }

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
                onNoteClick = { note ->
                    navController.navigate(Screen.AddEditWithArgs.createRoute(note.id))
                },
                onAddNoteClick = {
                    navController.navigate(Screen.AddEdit.route)
                },
                onDelete = { note ->
                    viewModel.updateNote(note.copy(isTrashed = true))
                },
                onArchive = { note ->
                    viewModel.updateNote(note.copy(isArchived = true))
                }
            )
        }
        composable(Screen.AddEditWithArgs.route) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toIntOrNull()
            val note = viewModel.notes.collectAsState().value.find { it.id == noteId }

            if (note != null) {
                AddEditNoteScreen(
                    viewModel = viewModel,
                    noteToEdit = note,
                    onSave = { navController.popBackStack() }
                )
            }
        }

        composable(Screen.AddEdit.route) {
            AddEditNoteScreen(
                viewModel = viewModel,
                onSave = { navController.popBackStack() }
            )
        }
    }
}