package com.example.masterworkcapstoneproject.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val quizRepository: QuizRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineQuizRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    override val quizRepository: QuizRepository by lazy {
        QuizRepository(
            QuizDatabase.getInstance(context.applicationContext).questionDao(),
            QuizDatabase.getInstance(context.applicationContext).answerDao()
        )
    }
}
