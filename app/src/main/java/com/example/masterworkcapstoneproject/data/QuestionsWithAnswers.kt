package com.example.masterworkcapstoneproject.data

import androidx.room.Embedded
import androidx.room.Relation

data class QuestionWithAnswers(
    @Embedded val question: Question,
    @Relation(
        parentColumn = "id", // Column in the Question table
        entityColumn = "questionId" // Column in the Answer table
    )
    val answers: List<Answer>
)



