package com.example.quizapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

enum class Screen { LOGIN, QUESTIONS }

data class QuizUIState(
    val username: String = "No name",
    val questionNumber: Int = 0,
    val score: Int = 0,
    val screen: Screen = Screen.LOGIN,
)

class QuizViewModel(
    userRepo: UsersRepository,
) : ViewModel() {
    var uiState by mutableStateOf(QuizUIState())
        private set

    val questions = listOf(
        QuizQuestion(R.drawable. flag_italy, listOf("Italy", "Ireland" ), "Italy"),
        QuizQuestion(R.drawable. flag_france, listOf("Netherlands" , "France" ), "France" ),
        QuizQuestion(R.drawable. flag_germany, listOf("Germany" , "Belgium" ), "Germany" )
    )

    fun onAnswer(option: String): Unit {
        val correct = (option == questions[uiState.questionNumber].correctAnswer)
        uiState = uiState.copy(
            questionNumber = uiState.questionNumber + 1,
            score = if (correct) uiState.score + 1 else uiState.score
        )
    }

    fun onLogin(username: String) {
        uiState = uiState.copy(
            username = username,
            screen = Screen.QUESTIONS,
        )
    }

    fun restart() {
        uiState =QuizUIState()
    }
}

interface UsersRepository {
    suspend fun addUsers(vararg u: User)
    suspend fun getPassword(username: String): String?
    fun getLeaderboard(): Flow<List<LeaderboardEntry>>
}