package com.example.sumincomposition.domain.entity_models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize              //эту аннотацию написали чтобы не писать все что находится в комментарии
data class GameSettings(
    val maxSumValue: Int,
    val minCountOfRightAnswers: Int,
    val minPercentOfRightAnswers: Int,
    val gameTimeInSeconds: Int
) : Parcelable

/*{

//а весь этот код нужен для того чтобы быстрее раблотало приложение чем Serializable
//а serializable и parcelable нужны для того чтоыб передавать обьекты во фрагментах или активити
// по умолчанию у нас есть только putInt, putString...
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(maxSumValue)
        parcel.writeInt(minCountOfRightAnswers)
        parcel.writeInt(minPercentOfRightAnswers)
        parcel.writeInt(gameTimeInSeconds)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GameSettings> {
        override fun createFromParcel(parcel: Parcel): GameSettings {
            return GameSettings(parcel)
        }

        override fun newArray(size: Int): Array<GameSettings?> {
            return arrayOfNulls(size)
        }
    }
}*/