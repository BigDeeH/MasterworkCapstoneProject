package com.example.masterworkcapstoneproject.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AnswerDao {
    @Insert
    suspend fun insertAnswer(answer: Answer)

    @Query("SELECT * FROM Answer WHERE questionId = :questionId")
    suspend fun getAnswersForQuestion(questionId: Long): List<Answer>

    @Query("DELETE FROM Answer")
    suspend fun clearAllAnswers()
}
