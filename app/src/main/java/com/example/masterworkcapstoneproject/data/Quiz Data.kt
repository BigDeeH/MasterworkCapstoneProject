package com.example.masterworkcapstoneproject.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val quizName: String, // Add quiz name to associate questions with a specific quiz
    val questionText: String
)


@Entity
data class Answer(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val questionId: Long,
    val answerText: String = "",
    val isCorrect: Boolean = false
)

