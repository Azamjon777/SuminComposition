package com.example.sumincomposition.domain.entity_models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameResult(
    val winner: Boolean,
    val countOfRightAnswers: Int,
    val countOfQuestions: Int,
    val gameSettings: GameSettings
) : Parcelable {
    val countOfRightAnswersString: String
        get() = countOfRightAnswers.toString()
}