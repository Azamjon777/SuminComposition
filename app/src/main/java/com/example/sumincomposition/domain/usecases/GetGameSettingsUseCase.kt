package com.example.sumincomposition.domain.usecases

import com.example.sumincomposition.domain.entity_models.GameSettings
import com.example.sumincomposition.domain.entity_models.Level
import com.example.sumincomposition.domain.repository.GameRepository

class GetGameSettingsUseCase(
    private val repository: GameRepository
) {
    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }
}