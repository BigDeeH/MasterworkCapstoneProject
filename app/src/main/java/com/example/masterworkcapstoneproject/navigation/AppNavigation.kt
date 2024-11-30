package com.example.masterworkcapstoneproject.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.masterworkcapstoneproject.DashboardDestination
import com.example.masterworkcapstoneproject.DashboardScreen
import com.example.masterworkcapstoneproject.HomeDestination
import com.example.masterworkcapstoneproject.HomeScreen
import com.example.masterworkcapstoneproject.QuestionViewModel
import com.example.masterworkcapstoneproject.QuestionViewModelFactory
import com.example.masterworkcapstoneproject.QuizDestination
import com.example.masterworkcapstoneproject.QuizDetailsScreen
import com.example.masterworkcapstoneproject.QuizScreen
import com.example.masterworkcapstoneproject.data.QuizRepository

@Composable
fun AppNavHost(
    navController: NavHostController,
    repository: QuizRepository
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route // Ensure this matches your start destination
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                onNavigateToDashboard = { navController.navigate(DashboardDestination.route) },
                onNavigateToQuiz = { navController.navigate(QuizDestination.route) }
            )
        }

        composable(route = DashboardDestination.route) {
            val viewModel: QuestionViewModel = viewModel(factory = QuestionViewModelFactory(repository))
            DashboardScreen(
                onNavigateToHome = { navController.navigate(HomeDestination.route) },
                onQuizSelected = { quizName ->
                    if (quizName.isNotEmpty()) {
                        navController.navigate("quizDetails/$quizName")
                    } else {
                        Log.e("AppNavHost", "Quiz name is empty!")
                    }
                },
                viewModel = viewModel
            )
        }

        composable("quizDetails/{quizName}") { backStackEntry ->
            val quizName = backStackEntry.arguments?.getString("quizName") ?: ""
            val viewModel: QuestionViewModel = viewModel(factory = QuestionViewModelFactory(repository))
            QuizDetailsScreen(
                onNavigateToHome = {navController.navigate(HomeDestination.route)},
                quizName = quizName,
                questionsAndAnswers = viewModel.quizDetails.collectAsState().value,
                viewModel = viewModel
            )
        }

        composable(route = QuizDestination.route) {
            QuizScreen(
                onNavigateToHome = { navController.navigate(HomeDestination.route) },
                onNavigateToDashboard = { navController.navigate(DashboardDestination.route) }
            )
        }
    }
}

