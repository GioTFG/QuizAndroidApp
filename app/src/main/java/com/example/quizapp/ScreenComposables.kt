package com.example.quizapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun LoginScreen(
    onLogin: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Inside LoginScreen
    val usernameState = rememberTextFieldState()
    val passwordState = rememberTextFieldState()


    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Flag Quiz App!")
        OutlinedTextField(
            state = usernameState,
            label = { Text("Username") },
            lineLimits = TextFieldLineLimits.SingleLine
        )
        OutlinedSecureTextField(
            state = passwordState,
            label = { Text("Password") }
        )

        Spacer(
            modifier = Modifier.size(24.dp)
        )
        ElevatedButton(
            onClick = {
                onLogin(usernameState.text.toString().trim(), passwordState.text.toString())
            },
            enabled = usernameState.text.isNotBlank()
                    && passwordState.text.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(50.dp, 0.dp)
        ) { Text("Login") }
    }
}

@Composable
fun QuizScreen(
    questions: List<QuizQuestion>,
    uiState: QuizUIState,
    onAnswer: (String) -> Unit,
) {
    val username: String = uiState.username
    val questionNumber: Int = uiState.questionNumber
    val score: Int = uiState.score

    if (questionNumber < questions.size) {
        FlagQuestion(
            questions[questionNumber],
            onAnswer = onAnswer,
            modifier = Modifier.fillMaxSize(0.5f)
        )
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "Congratulations, $username!\n" +
                        "You finished the quiz!\n" +
                        "$score/${questions.size} correct answers!"
            )
        }
    }
}

