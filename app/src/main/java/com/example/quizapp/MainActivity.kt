/**
 * @author Giovanni Ancora
 * Matricola UniPR: 391457
 * Created on: 04/11/2026
 * Github: https://github.com/GioTFG/QuizAndroidApp
 */

package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.quizapp.ui.theme.QuizAppTheme

// ---------- Data ----------
data class QuizQuestion(
    @DrawableRes val flagRes: Int,
    val options: List<String>,
    val correctAnswer : String
)

data class QuizUIState(
    val questionNumber: Int = 0,
    val score: Int = 0
)

class QuizViewModel: ViewModel() {
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
}

class MainActivity : ComponentActivity() {
    // The PNG files must be placed in res/drawable with these names


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val vm: QuizViewModel = viewModel(
                        factory = viewModelFactory { initializer { QuizViewModel() } }
                    )

                    QuizApp(
                        vm,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun QuizApp(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier,
) {
    var username by rememberSaveable { mutableStateOf("") }
    var loggedIn by rememberSaveable { mutableStateOf(false) }

    Box() {
        Surface(modifier = Modifier
            .fillMaxSize()
            .align(Alignment.Center)
        ) {
            if (!loggedIn) {
                LoginScreen(
                    onLogin = { name ->
                        username = name
                        loggedIn = true
                    }
                )
            } else {
                QuizScreen(
                    username,
                    viewModel.questions,
                    viewModel.uiState.questionNumber,
                    viewModel.uiState.score,
                    viewModel::onAnswer,
                )
                // Text("Loggato")
            }
        }
        Text("Giovanni Ancora, #391457, 16/11/2026", modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(0.dp, 0.dp, 10.dp, 50.dp)
        )
    }
}

@Composable
fun LoginScreen(
    onLogin: (String) -> Unit,
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
                onLogin(usernameState.text.toString().trim())
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
fun FlagQuestion(
    question: QuizQuestion,
    onAnswer: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Which country is this flag from?")
        Spacer(modifier = Modifier.size(24.dp))
        Image(
            painter = painterResource(question.flagRes),
            contentDescription = "Flag to guess",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        )
        Spacer(modifier = Modifier.size(24.dp))

        if (question.options.size == 2) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ElevatedButton( onClick = { onAnswer(question.options[0]) } ) {
                    Text( question.options[0])
                }
                Spacer(Modifier.size(12.dp))
                ElevatedButton( onClick = { onAnswer(question.options[1]) } ) {
                    Text( question.options[1])
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                for (q in question.options) {
                    ElevatedButton(
                        onClick = { onAnswer(q) }
                    ) { Text(q) }
                }
            }
        }
    }
}

@Composable
fun QuizScreen(
    username: String,
    questions: List<QuizQuestion>,
    questionNumber: Int,
    score: Int,
    onAnswer: (String) -> Unit,
) {
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