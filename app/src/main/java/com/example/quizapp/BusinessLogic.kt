package com.example.quizapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

enum class Screen { LOGIN, REGISTER, QUESTIONS }

data class QuizUIState(
    val loginStatus: String? = null,
    val registerStatus: String? = null,

    val username: String = "No name",
    val questionNumber: Int = 0,
    val score: Int = 0,
    val screen: Screen = Screen.LOGIN,
)

class QuizViewModel(
    private val userRepo: UsersRepository,
) : ViewModel() {
    var uiState by mutableStateOf(QuizUIState())
        private set

    val questions = listOf(
        QuizQuestion(R.drawable.flag_italy, listOf("Italy", "Ireland"), "Italy"),
        QuizQuestion(R.drawable.flag_france, listOf("Netherlands", "France"), "France"),
        QuizQuestion(R.drawable.flag_germany, listOf("Germany", "Belgium"), "Germany")
    )

    fun onAnswer(option: String): Unit {
        val correct = (option == questions[uiState.questionNumber].correctAnswer)
        uiState = uiState.copy(
            questionNumber = uiState.questionNumber + 1,
            score = if (correct) uiState.score + 1 else uiState.score
        )
    }

    fun onLogin(username: String, password: String) {
        viewModelScope.launch {
            val dbPassword = userRepo.getPassword(username)
            uiState = when (dbPassword) {
                null -> uiState.copy(
                    loginStatus = "User not found",
                )

                password -> uiState.copy(
                    loginStatus = "Login succeeded",
                    username = username,
                    screen = Screen.QUESTIONS,
                )

                else -> uiState.copy(
                    loginStatus = "Wrong password"
                )
            }
        }
    }

    fun onRegister(username: String, password: String) {
        viewModelScope.launch {
            val dbPassword = userRepo.getPassword(username)
            when (dbPassword) {
                null -> {
                    userRepo.addUsers(User(username = username, password = password))
                    uiState = uiState.copy(
                        registerStatus = "Register succeeded",
                        screen = Screen.LOGIN,
                    )
                }

                else -> {
                    uiState = uiState.copy(
                        registerStatus = "Username not available"
                    )
                }
            }
        }
    }

    fun onGoLogin() {
        restart()
    }
    fun onGoRegister() {
        uiState = uiState.copy(
            registerStatus = null,
            screen = Screen.REGISTER
        )
    }

    fun restart() {
        uiState = QuizUIState()
    }
}

interface UsersRepository {
    suspend fun addUsers(vararg u: User)
    suspend fun getPassword(username: String): String?
    fun getLeaderboard(): Flow<List<LeaderboardEntry>>
}