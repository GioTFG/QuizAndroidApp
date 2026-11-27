package com.example.quizapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.collections.listOf

enum class Screen { LOGIN, REGISTER, QUESTIONS, LEADERBOARD }

data class QuizUIState(
    val loginStatus: String? = null,
    val registerStatus: String? = null,

    val questions: List<QuizQuestion> = listOf(
        QuizQuestion(R.drawable.flag_italy, listOf("Italy", "Ireland"), "Italy"),
        QuizQuestion(R.drawable.flag_france, listOf("Netherlands", "France"), "France"),
        QuizQuestion(R.drawable.flag_germany, listOf("Germany", "Belgium"), "Germany")
    ),

    val leaderboard: List<LeaderboardEntry> = listOf(),

    val username: String = "No name",
    val questionNumber: Int = 0,
    val score: Int = 0,
    val screen: Screen = Screen.LOGIN,
)

class QuizViewModel(
    private val userRepo: UsersRepository,
) : ViewModel() {
    var uiState by mutableStateOf(
        QuizUIState(

        )
    )
        private set

    val leaderboardFlow: Flow<List<LeaderboardEntry>> = userRepo.observeLeaderboard()

    init {
        viewModelScope.launch {
            leaderboardFlow.collect { list ->
                uiState = uiState.copy(
                    leaderboard = list
                )
            }
        }
    }

    fun onAnswer(option: String): Unit {
        val correct = (option == uiState.questions[uiState.questionNumber].correctAnswer)
        val newScore = if (correct) uiState.score + 1 else uiState.score

        val isLast = uiState.questionNumber == uiState.questions.size - 1

        when (isLast) {
            true -> {
                uiState = uiState.copy(
                    score = newScore,
                    screen = Screen.LEADERBOARD
                )

                viewModelScope.launch {
                    val currentHighScore = userRepo.getHighScore(uiState.username)
                        ?: throw Exception("Impossible case: username not present when somebody is logged in!")
                    //TODO: Possibile cosa da fare, gestire gli errori con una schermata di errore
                    //      che resetta al login e basta

                    if (uiState.score > currentHighScore)
                        userRepo.updateHighScore(uiState.username, uiState.score)
                }
            }

            false -> {
                uiState = uiState.copy(
                    score = newScore,
                    questionNumber = uiState.questionNumber + 1
                )
            }
        }
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
            screen = Screen.REGISTER,
            leaderboard = uiState.leaderboard
        )
    }

    fun restart() {
        uiState = QuizUIState(
            leaderboard = uiState.leaderboard
        )
    }
}

interface UsersRepository {
    suspend fun addUsers(vararg u: User)
    suspend fun getPassword(username: String): String?
    fun observeLeaderboard(): Flow<List<LeaderboardEntry>>
    suspend fun updateHighScore(username: String, newHighScore: Int)
    suspend fun getHighScore(username: String): Int?
}