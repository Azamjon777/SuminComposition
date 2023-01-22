package com.example.sumincomposition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sumincomposition.R
import com.example.sumincomposition.data.GameRepositoryImpl
import com.example.sumincomposition.domain.entity_models.GameResult
import com.example.sumincomposition.domain.entity_models.GameSettings
import com.example.sumincomposition.domain.entity_models.Level
import com.example.sumincomposition.domain.entity_models.Question
import com.example.sumincomposition.domain.usecases.GenerateQuestionUseCase
import com.example.sumincomposition.domain.usecases.GetGameSettingsUseCase

//будем использовать AndroidViewModel() чтобы получить доступ к строковым ресурсам
class GameViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var level: Level

    //в переменную gameSettings приходит значение из GetGameSettingsUseCase
    private lateinit var gameSettings: GameSettings

    private val context = application

    private val repository = GameRepositoryImpl
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)
    private var timer: CountDownTimer? = null

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRightAnswers: LiveData<Int>
        get() = _percentOfRightAnswers

    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    private val _enoughCount = MutableLiveData<Boolean>()
    val enoughCount: LiveData<Boolean>
        get() = _enoughCount

    private val _enoughPercent = MutableLiveData<Boolean>()
    val enoughPercent: LiveData<Boolean>
        get() = _enoughPercent

    private val _minPercentSecondaryProgress = MutableLiveData<Int>()
    val minPercentSecondaryProgress: LiveData<Int>
        get() = _minPercentSecondaryProgress

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private var countOfRightAnswers = 0
    private var countOfQuestions = 0


    fun startGame(level: Level) {
        getGameSettings(level)
        startTimer()
        generateQuestion()
        updateProgress()
    }

    fun chooseAnswer(number: Int) {
        generateQuestion()  //при каждом вызове этой функции, _question заного инициализируется
        checkAnswer(number)
        updateProgress()
    }

    private fun updateProgress() {
        val percent = calculatePercentOfRightAnswers()
        _percentOfRightAnswers.value = percent
        _progressAnswers.value = String.format(
            context.resources.getString(R.string.right_answers),
            countOfRightAnswers,
            gameSettings.minCountOfRightAnswers
        )

        //снизу минимальный кол-во правильных ответов которые необходимы для победы типа Boolean
        _enoughCount.value =
            countOfRightAnswers >= gameSettings.minCountOfRightAnswers

        //снизу минимальный процент который необходим для победы типа Boolean
        _enoughPercent.value = percent >= gameSettings.minPercentOfRightAnswers
    }

    private fun calculatePercentOfRightAnswers(): Int {
        if (countOfQuestions == 0){
            return 0
        }
        //если бы мы не привели один их них к типу toDouble(toLong), то ответ всегда был бы 1 или 0
        // так как int / int = целое число
        return ((countOfRightAnswers.toDouble() / countOfQuestions) * 100).toInt()
    }


    private fun checkAnswer(number: Int) {
        // используется question, а не _question для того чтобы вся логика была в GameViewModel
        val rightAnswer = question.value?.rightAnswer   //здесь sum вычитается от visibleNumber
        if (number == rightAnswer) {
            countOfRightAnswers++
        }
        countOfQuestions++
    }


    private fun getGameSettings(level: Level) {
        this.level = level
        this.gameSettings = getGameSettingsUseCase.invoke(level)
        //здесь модель gameSettings уже имеющимися своими настройками уровня сложностей

        _minPercentSecondaryProgress.value = gameSettings.minPercentOfRightAnswers
    }


    private fun startTimer() {
        timer = object : CountDownTimer(
            gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS, MILLIS_IN_SECONDS
        ) {
            override fun onTick(millisUntilFinished: Long) {
                //В _formattedTime хранится уже преобразованный String типа '00:00'
                _formattedTime.value = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer?.start()
    }


    private fun generateQuestion() {
        //здесь generateQuestionUseCase возвращает полностью модельку Question
        // и еще за одно только что проинициализованный gameSettings с maxSumValue передается
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }


    private fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECONDS //общее кол-во секунд
        val minutes = seconds / SECONDS_IN_MINUTES    // общее кол-во минут
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)// оставщиеся секунды
        return String.format("%02d:%02d", minutes, leftSeconds)
        //сверху мы минуты и секунды преобразуем типа 00:00
    }


    private fun finishGame() {
        //когда таймер закончится, запустится эта функция
        //здесь уже готовые результаты, но запускать GameFinishedFragment запустит GameFragment\
        // значения передаем в лайфдату _gameResult
        _gameResult.value = GameResult(
            winner = enoughCount.value == true && enoughPercent.value == true,
            countOfRightAnswers = countOfRightAnswers,
            countOfQuestions = countOfQuestions,
            gameSettings = gameSettings
        )
    }


    override fun onCleared() {
        //когда мы уходим из фрагменты необходимо чтобы timer() отменялся в методе onCleared()
        super.onCleared()
        timer?.cancel()
    }


    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTES = 60
    }
}