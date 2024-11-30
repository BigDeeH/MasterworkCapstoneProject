package com.example.masterworkcapstoneproject

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.masterworkcapstoneproject.data.Answer
import com.example.masterworkcapstoneproject.data.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.masterworkcapstoneproject.data.QuizRepository

class QuestionViewModel(
    private val quizRepository: QuizRepository
) : ViewModel() {

    val quizName = mutableStateOf("") // Holds the current quiz name
    val questions = mutableStateListOf<Pair<MutableState<String>, SnapshotStateList<String>>>()

    private val _savedQuizzes = MutableStateFlow<List<String>>(emptyList()) // List of saved quizzes
    val savedQuizzes: StateFlow<List<String>> = _savedQuizzes

    private val _quizDetails = MutableStateFlow<List<Pair<String, List<String>>>>(emptyList()) // Details for a specific quiz
    val quizDetails: StateFlow<List<Pair<String, List<String>>>> = _quizDetails

    // Add a new question with an empty answer list
    fun addQuestion() {
        questions.add(Pair(mutableStateOf(""), SnapshotStateList()))
    }

    // Add a new answer to a specific question
    fun addAnswerToQuestion(questionIndex: Int) {
        if (questionIndex in questions.indices) {
            questions[questionIndex].second.add("")
        }
    }

    // Check if all questions and answers are valid
    fun isValidInput(): Boolean {
        return questions.all { (question, answers) ->
            question.value.isNotBlank() && answers.all { it.isNotBlank() }
        }
    }

    // Save all questions and answers to the database under the current quiz name
    fun saveAllToDatabase() {
        if (quizName.value.isBlank()) {
            Log.w("QuestionViewModel", "Quiz name is blank")
            return
        }

        if (questions.isEmpty()) {
            Log.w("QuestionViewModel", "No questions to save")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                questions.forEach { (questionState, answersState) ->
                    val questionEntity = Question(
                        quizName = quizName.value, // Ensure quizName is set
                        questionText = questionState.value
                    )

                    val questionId = quizRepository.saveQuestion(questionEntity)

                    answersState.forEach { answer ->
                        val answerEntity = Answer(
                            questionId = questionId,
                            answerText = answer,
                            isCorrect = true // Adjust logic as needed
                        )
                        quizRepository.saveAnswer(answerEntity)
                    }
                }
                fetchSavedQuizzes()
                clearDynamicQuestions()
                Log.d("QuestionViewModel", "All questions saved successfully under quiz '${quizName.value}'")
            } catch (e: Exception) {
                Log.e("QuestionViewModel", "Error saving to database: ${e.message}", e)
            }
        }
    }

    // Fetch the list of all saved quiz names
    fun fetchSavedQuizzes() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _savedQuizzes.value = quizRepository.getSavedQuizzes()
            } catch (e: Exception) {
                Log.e("QuestionViewModel", "Error fetching quizzes: ${e.message}", e)
            }
        }
    }

    fun fetchQuizDetails(quizName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Fetch data from repository
                val quizData = quizRepository.getQuestionsAndAnswersForQuiz(quizName)

                // Map the fetched data to the desired structure
                _quizDetails.value = quizData.map { questionWithAnswers ->
                    questionWithAnswers.question.questionText to questionWithAnswers.answers.map { it.answerText }
                }

                Log.d("QuestionViewModel", "Quiz details fetched for: $quizName")
            } catch (e: Exception) {
                Log.e("QuestionViewModel", "Error fetching quiz details: ${e.message}", e)
            }
        }
    }

    // Clear all saved quizzes in the repository
    fun clearAllQuizzes() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                quizRepository.clearAllQuizzes()
                _savedQuizzes.value = emptyList()
                Log.d("QuestionViewModel", "All quizzes cleared successfully.")
            } catch (e: Exception) {
                Log.e("QuestionViewModel", "Error clearing quizzes: ${e.message}", e)
            }
        }
    }

    // Clear all questions and reset the quiz name
    private fun clearDynamicQuestions() {
        questions.clear()
        quizName.value = ""
    }
}


