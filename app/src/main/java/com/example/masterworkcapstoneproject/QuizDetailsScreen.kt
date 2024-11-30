package com.example.masterworkcapstoneproject

import android.content.Context
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizDetailsScreen(
    quizName: String,
    questionsAndAnswers: List<Pair<String, List<String>>>,
    viewModel: QuestionViewModel,
    onNavigateToHome: () -> Unit = {}
) {
    // Fetch quiz details when this screen is launched
    LaunchedEffect(quizName) {
        viewModel.fetchQuizDetails(quizName)
    }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(quizName, color = Color.White) },
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                actions = {
                    IconButton(onClick = { onNavigateToHome() }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_home_24),
                            contentDescription = "Home",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = {
                        val htmlContent = generateQuizHtml(quizName, questionsAndAnswers)
                        printHtmlQuiz(context, quizName, htmlContent)
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_print_24),
                            contentDescription = "Print Quiz",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(questionsAndAnswers) { (question, answers) ->
                Text(
                    text = question,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                answers.forEachIndexed { index, answer ->
                    Text(
                        text = "${'A' + index}. $answer",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

/**
 * Function to generate the HTML content for the quiz
 */
fun generateQuizHtml(quizName: String, questionsAndAnswers: List<Pair<String, List<String>>>): String {
    val stringBuilder = StringBuilder()
    stringBuilder.append("""
        <html>
        <head>
            <style>
                body {
                    margin: 1cm; /* 1 cm margins */
                    font-family: Arial, sans-serif;
                }
                h1 {
                    text-align: center;
                }
                p {
                    margin-bottom: 0.5cm;
                }
            </style>
        </head>
        <body>
    """)
    stringBuilder.append("<h1>Quiz: $quizName</h1>")

    questionsAndAnswers.forEachIndexed { index, (question, answers) ->
        stringBuilder.append("<p><b>${index + 1}. $question</b></p>")
        answers.forEachIndexed { answerIndex, answer ->
            stringBuilder.append("<p style='margin-left:20px;'>${'A' + answerIndex}. $answer</p>")
        }
    }

    stringBuilder.append("</body></html>")
    return stringBuilder.toString()
}


/**
 * Function to print the quiz using a WebView
 */
fun printHtmlQuiz(context: Context, jobName: String, htmlContent: String) {
    val webView = WebView(context)
    webView.webViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
            val printAdapter: PrintDocumentAdapter = webView.createPrintDocumentAdapter(jobName)

            // Set up print attributes for A4 paper with custom margins
            val printAttributes = PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4) // A4 paper size
                .setResolution(PrintAttributes.Resolution("default", "default", 300, 300)) // 300 DPI
                .setMinMargins(PrintAttributes.Margins(1000, 1000, 1000, 1000)) // Margins: 1 cm (1000 microns)
                .build()

            printManager.print(jobName, printAdapter, printAttributes)
        }
    }

    webView.loadDataWithBaseURL(null, htmlContent, "text/HTML", "UTF-8", null)
}