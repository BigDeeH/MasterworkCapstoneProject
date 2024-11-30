package com.example.masterworkcapstoneproject

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            // Retrieve the application instance
            val application = this.quizApplication()

            // Pass necessary dependencies to QuestionViewModel
            QuestionViewModel(
                quizRepository = application.container.quizRepository
            )
        }
    }
}

// Extension function for accessing QuizApplication from CreationExtras
fun CreationExtras.quizApplication(): QuizApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as QuizApplication)
