package com.zmt.kotlin_wla.mvp.view

import android.view.ViewGroup

/**
 * View Interface to allow the communication between the Main Game Manager and the Game view
 */
interface IGameView {
    fun addQuizQuestionFragment(pQuizQuestionFragment: QuizQuestionFragment)
    fun correctAnswerGiven()
    fun incorrectAnswerGiven()
    fun noAnswerGiven()
    fun questionTimeOver(pSelectedAnswer: String)
    fun onGameOver(pCorrectAnswers : Int, pIncorrectAnswers : Int, mGameType : String, mGameCategory : String, mGameDifficulty : String)
    fun increaseQuestionCounter(pTotalQuestions : Int)
    fun setViewEnabled(pViewGroup: ViewGroup?, pEnabled: Boolean)
}