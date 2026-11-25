/**
 * @author Giovanni Ancora
 * Matricola UniPR: 391457
 * Created on: 04/11/2026
 * Github: https://github.com/GioTFG/QuizAndroidApp
 */

package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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



class MainActivity : ComponentActivity() {
    // The PNG files must be placed in res/drawable with these names


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val dao = UserDatabase.getInstance(applicationContext).getDao()
                    val usersRepo = DaoUsersRepository(dao)
                    val vm: QuizViewModel = viewModel(
                        factory = viewModelFactory { initializer { QuizViewModel(usersRepo) } }
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
    Box {
        Surface(modifier = Modifier
            .fillMaxSize()
            .align(Alignment.Center)
        ) {
            when (viewModel.uiState.screen) {
                Screen.LOGIN -> {
                    LoginScreen(
                        onLogin = viewModel::onLogin
                    )
                }
                Screen.QUESTIONS -> {
                    BackHandler(true, viewModel::restart)
                    QuizScreen(
                        viewModel.questions,
                        viewModel.uiState,
                        viewModel::onAnswer,
                    )
                }
            }
        }
        Text("Giovanni Ancora, #391457, 16/11/2026", modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(0.dp, 0.dp, 10.dp, 50.dp)
        )
    }
}
