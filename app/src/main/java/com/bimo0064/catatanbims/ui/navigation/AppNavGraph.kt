package com.bimo0064.catatanbims.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bimo0064.catatanbims.ui.screens.AboutScreen
import com.bimo0064.catatanbims.ui.screens.AddEditNoteScreen
import com.bimo0064.catatanbims.ui.screens.NoteListScreen
import com.bimo0064.catatanbims.viewmodel.NoteViewModel

sealed class Screen(val route: String) {
    object Notes : Screen("notes")
    object AddEdit : Screen("add_edit")
    object About : Screen("about")
    object AddEditWithArgs : Screen("add_edit/{noteId}") {
        fun createRoute(noteId: Int) = "add_edit/$noteId"
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: NoteViewModel,
) {
    NavHost(navController, startDestination = Screen.Notes.route) {
        composable(Screen.Notes.route) {
            NoteListScreen(
                viewModel = viewModel,
                onNoteClick = { note ->
                    navController.navigate(Screen.AddEditWithArgs.createRoute(note.id))
                },
                onAddNoteClick = { navController.navigate(Screen.AddEdit.route) },
                onDelete = { note -> viewModel.deleteNote(note) },
                onArchive = { note -> viewModel.archiveNote(note) },
                onRestore = { note -> viewModel.restoreNote(note) },
                onPermanentDelete = { note -> viewModel.permanentlyDeleteNote(note) },
                onAboutClick = { navController.navigate(Screen.About.route) } // tambahkan ini
            )
        }
        composable(Screen.AddEdit.route) {
            AddEditNoteScreen(
                note = null,
                onSave = { note ->
                    viewModel.addNote(note)
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }
        composable(Screen.AddEditWithArgs.route) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toIntOrNull()
            val note = viewModel.notes.value.find { it.id == noteId }
            AddEditNoteScreen(
                note = note,
                onSave = { updatedNote ->
                    viewModel.updateNote(updatedNote)
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }
        composable(Screen.About.route) {
            AboutScreen(onBackClick = { navController.popBackStack() })
        }
    }
}