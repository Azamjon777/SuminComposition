package com.example.sumincomposition.domain.entity_models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Level : Parcelable {
    TEST, EASY, NORMAL, HARD
}