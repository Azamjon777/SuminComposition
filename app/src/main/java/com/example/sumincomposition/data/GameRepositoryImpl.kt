package com.example.sumincomposition.data

import com.example.sumincomposition.domain.entity_models.GameSettings
import com.example.sumincomposition.domain.entity_models.Level
import com.example.sumincomposition.domain.entity_models.Question
import com.example.sumincomposition.domain.repository.GameRepository
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

object GameRepositoryImpl : GameRepository {

    private const val MIN_SUM_VALUE = 2
    private const val MIN_ANSWER_VALUE = 1

    override fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question {
        val sum = Random.nextInt(MIN_SUM_VALUE, maxSumValue + 1)               //сумма сложения
        val visibleNumber = Random.nextInt(MIN_ANSWER_VALUE, sum)            //тот что на квадратике
        val options = HashSet<Int>()                                         //проверяет на дубликат
        val rightAnswer = sum - visibleNumber                                     //правильный ответ

        options.add(rightAnswer)

        //диопазоны ответов снизу
        val from = max(rightAnswer - countOfOptions, MIN_ANSWER_VALUE)  //тут минимум 1 и более
        val to = min(maxSumValue, rightAnswer + countOfOptions)    //тут потолок ответа
        while (options.size < countOfOptions) {
            options.add(Random.nextInt(from, to))  //добавляем недостающие 5 шт ответов
        }
        return Question(sum, visibleNumber, options.toList())
    }

    override fun getGameSettings(level: Level): GameSettings {
        return when (level) {
            Level.TEST -> GameSettings(
                10,
                7,
                70,
                10
            )

            Level.EASY -> GameSettings(
                10,
                10,
                80,
                60
            )

            Level.NORMAL -> GameSettings(
                15,
                7,
                80,
                50
            )

            Level.HARD -> GameSettings(
                20,
                10,
                60,
                40
            )
        }
    }
}