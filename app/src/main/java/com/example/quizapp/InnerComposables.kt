package com.example.quizapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

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

