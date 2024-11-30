package com.example.masterworkcapstoneproject

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.masterworkcapstoneproject.navigation.NavigationDestination

object DashboardDestination : NavigationDestination {
    override val route = "dashboard"
    override val titleRes = R.string.dashboard
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToHome: () -> Unit = {},
    onQuizSelected: (String) -> Unit = {}, // Callback to navigate to quiz details
    viewModel: QuestionViewModel, // Accept the shared ViewModel as a parameter
) {
    // Collect the saved quizzes
    val savedQuizzes by viewModel.savedQuizzes.collectAsState()

    // Fetch quizzes when the screen is displayed
    LaunchedEffect(Unit) {
        viewModel.fetchSavedQuizzes()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Saved Quizzes", color = Color.White) },
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                actions = {
                    // Navigate to Home Button
                    IconButton(onClick = { onNavigateToHome() }) {
                        Image(
                            painter = painterResource(R.drawable.baseline_home_24),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                    // Clear All Button
                    IconButton(onClick = { viewModel.clearAllQuizzes() }) {
                        Image(
                            painter = painterResource(R.drawable.baseline_delete_24),
                            contentDescription = "Clear All",
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Display quizzes
            items(savedQuizzes) { quizName ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onQuizSelected(quizName) }
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Text(
                        text = quizName,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}






