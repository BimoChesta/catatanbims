//package com.bimo0064.catatanbims
//
//import androidx.lifecycle.ViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//
//class ThemeViewModel : ViewModel() {
//    private val _isDarkTheme = MutableStateFlow(false)
//    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme
//
//    fun toggleTheme() {
//        _isDarkTheme.value = !_isDarkTheme.value
//    }
//}