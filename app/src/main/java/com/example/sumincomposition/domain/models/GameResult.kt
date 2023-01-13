package com.example.sumincomposition.domain.models

data class GameResult(
    val winner: Boolean,
    val countOfRightAnswers: Int,
    val countOfQuestions: Int, //кол-во вопросов на которое мы ответили
    val gameSettings: GameSettings
)