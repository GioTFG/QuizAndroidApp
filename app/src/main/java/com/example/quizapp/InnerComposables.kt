package com.example.quizapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizapp.ui.theme.QuizAppTheme

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
fun Leaderboard(
    uiState: QuizUIState,
    leaderboard: List<LeaderboardEntry>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Leaderboard")
        Spacer(Modifier.size(12.dp))
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Text("Rank", modifier = Modifier.weight(0.3f), textAlign = TextAlign.Center)
            Text("Username", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Text("High Score", modifier = Modifier.weight(0.5f), textAlign = TextAlign.Center)
        }
        LazyColumn(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            itemsIndexed(leaderboard) { index, it ->
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Text("#${index+1}", modifier = Modifier.weight(0.3f), textAlign = TextAlign.Center)
                    Text(it.username, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                    Text("${it.highScore}", modifier = Modifier.weight(0.5f), textAlign = TextAlign.Center)
                }
            }
        }
    }
}


@Preview(name = "Leaderboard", showBackground = true)
@Composable
fun LeaderboardPreview() {
    QuizAppTheme {
        Box(Modifier.fillMaxSize()) {
            Leaderboard(
                QuizUIState(),
                listOf(
                    LeaderboardEntry("Gino", 10),
                    LeaderboardEntry("Provolo", 7),
                    LeaderboardEntry("Pinco", 6),
                    LeaderboardEntry("Pippo", 3)
                ),
                Modifier.fillMaxSize()
            )
        }
    }
}