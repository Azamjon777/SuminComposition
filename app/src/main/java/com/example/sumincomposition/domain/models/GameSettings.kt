package com.example.sumincomposition.domain.models

data class GameSettings(
    val maxSumValue: Int,   // максимально возможное значение
    val minCountOfRightAnswers: Int, //сколько минимум нужно набрать правильных ответов
    val minPercentOfRightAnswers: Int,// минимальный процент правильных ответов (80%)
    val gameTimeINSeconds: Int // время игры в секундах
)