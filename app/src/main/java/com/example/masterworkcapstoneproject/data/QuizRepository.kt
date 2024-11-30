package com.example.masterworkcapstoneproject.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuizRepository(
    private val questionDao: QuestionDao,
    private val answerDao: AnswerDao
) {

    // Save a question to the database and return its ID
    suspend fun saveQuestion(question: Question): Long {
        return withContext(Dispatchers.IO) {
            questionDao.insertQuestion(question)
        }
    }

    // Save an answer to the database
    suspend fun saveAnswer(answer: Answer) {
        withContext(Dispatchers.IO) {
            answerDao.insertAnswer(answer)
        }
    }

    // Fetch all saved quiz names
    suspend fun getSavedQuizzes(): List<String> {
        return withContext(Dispatchers.IO) {
            questionDao.getDistinctQuizNames()
        }
    }

    // Fetch questions and answers for a specific quiz
    suspend fun getQuestionsAndAnswersForQuiz(quizName: String): List<QuestionWithAnswers> {
        return withContext(Dispatchers.IO) {
            questionDao.getQuestionsByQuizName(quizName)
        }
    }

    // Clear all data from the database
    suspend fun clearAllQuizzes() {
        withContext(Dispatchers.IO) {
            questionDao.clearAllQuestions()
            answerDao.clearAllAnswers()
        }
    }
}
