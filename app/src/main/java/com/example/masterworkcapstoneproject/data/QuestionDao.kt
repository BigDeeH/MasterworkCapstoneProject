package com.example.masterworkcapstoneproject.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface QuestionDao {
    @Insert
    suspend fun insertQuestion(question: Question): Long

    @Insert
    suspend fun insertAnswer(answer: Answer)

    @Query("SELECT DISTINCT quizName FROM Question")
    suspend fun getDistinctQuizNames(): List<String>

    @Transaction
    @Query("SELECT * FROM Question WHERE quizName = :quizName")
    suspend fun getQuestionsByQuizName(quizName: String): List<QuestionWithAnswers>


    @Query("DELETE FROM Question")
    suspend fun clearAllQuestions()

    @Query("DELETE FROM Answer")
    suspend fun clearAllAnswers()
}

