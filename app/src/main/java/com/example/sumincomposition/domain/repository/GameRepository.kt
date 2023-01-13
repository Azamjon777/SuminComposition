package com.example.sumincomposition.domain.repository

import com.example.sumincomposition.domain.models.GameSettings
import com.example.sumincomposition.domain.models.Level
import com.example.sumincomposition.domain.models.Question

interface GameRepository {
    fun generateQuestion(
        maxSumValue: Int,
        countOfOptions: Int
    ): Question

    fun getGameSettings(level: Level): GameSettings
}