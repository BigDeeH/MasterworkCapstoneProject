package com.example.masterworkcapstoneproject

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.masterworkcapstoneproject.data.RepositoryProvider
import com.example.masterworkcapstoneproject.navigation.NavigationDestination

object QuizDestination : NavigationDestination {
    override val route = "quiz"
    override val titleRes = R.string.quiz
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToDashboard: () -> Unit = {}
) {
    val context = LocalContext.current
    val repository = remember { RepositoryProvider.getRepository(context) }
    val viewModel: QuestionViewModel = viewModel(
        factory = QuestionViewModelFactory(repository)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Quiz", color = Color.White) },
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                actions = {
                    IconButton(onClick = { onNavigateToHome() }) {
                        Image(
                            painter = painterResource(R.drawable.baseline_home_24),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = viewModel.quizName.value,
                onValueChange = { viewModel.quizName.value = it },
                label = { Text("Enter Quiz Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Render questions dynamically
            viewModel.questions.forEachIndexed { index, (questionState, answersState) ->
                Question(
                    questionState = questionState,
                    answersState = answersState,
                    onAddAnswer = { viewModel.addAnswerToQuestion(index) }
                )
                Spacer(modifier = Modifier.height(25.dp))
            }

            // Add Question button
            Button(onClick = { viewModel.addQuestion() }, modifier = Modifier.fillMaxWidth()) {
                Text("Add Question")
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Save Button
            Button(
                onClick = {
                    if (!viewModel.isValidInput()) {
                        Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    viewModel.saveAllToDatabase()
                    Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show()
                    onNavigateToDashboard()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save All")
            }
        }
    }
}


@Composable
fun Question(
    questionState: MutableState<String>,
    answersState: SnapshotStateList<String>,
    onAddAnswer: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Question input
        Text(
            text = "Question:",
            style = MaterialTheme.typography.bodyMedium
        )

        OutlinedTextField(
            value = questionState.value,
            onValueChange = { questionState.value = it },
            label = { Text("Enter your question here") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Answers section
        Text(
            text = "Answers:",
            style = MaterialTheme.typography.bodyMedium
        )

        answersState.forEachIndexed { index, answer ->
            OutlinedTextField(
                value = answer,
                onValueChange = { newValue -> answersState[index] = newValue },
                label = { Text("Answer ${index + 1}") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

        // Button to add a new answer
        Button(
            onClick = onAddAnswer,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Add Answer")
        }
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Answer(onAnswerChange: (String) -> Unit) {
    val responseOptions = listOf("Radio Button", "Checkbox")
    var expanded by remember { mutableStateOf(false) }
    var selectedResponse by remember { mutableStateOf(responseOptions[0]) }
    var answerText by remember { mutableStateOf("") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedResponse,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            responseOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedResponse = option
                        expanded = false
                    }
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(25.dp))

    OutlinedTextField(
        value = answerText,
        onValueChange = {
            answerText = it
            onAnswerChange(it)
        },
        label = { Text("Enter Your Answer Here") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}