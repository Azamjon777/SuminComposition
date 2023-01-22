package com.example.sumincomposition.domain.entity_models

data class Question(
    val sum: Int,
    val visibleNumber: Int,
    val options: List<Int>
) {
    val rightAnswer: Int
        get() = sum - visibleNumber
}