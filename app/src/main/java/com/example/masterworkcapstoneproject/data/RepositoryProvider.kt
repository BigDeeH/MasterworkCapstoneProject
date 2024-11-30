package com.example.masterworkcapstoneproject.data

import android.content.Context

object RepositoryProvider {
    private var instance: QuizRepository? = null

    fun getRepository(context: Context): QuizRepository {
        return instance ?: synchronized(this) {
            val db = QuizDatabase.getInstance(context)
            val newRepo = QuizRepository(db.questionDao(), db.answerDao())
            instance = newRepo
            newRepo
        }
    }
}
