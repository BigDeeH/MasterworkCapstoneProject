package com.example.masterworkcapstoneproject

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.masterworkcapstoneproject.data.QuizDatabase
import com.example.masterworkcapstoneproject.data.QuizRepository
import com.example.masterworkcapstoneproject.navigation.AppNavHost

@Composable
fun QuizApp() {
    val context = LocalContext.current
    val database = QuizDatabase.getInstance(context)
    val repository = QuizRepository(
        questionDao = database.questionDao(),
        answerDao = database.answerDao()
    )

    val navController = rememberNavController()

    AppNavHost(
        navController = navController,
        repository = repository // Pass the repository here
    )
}

