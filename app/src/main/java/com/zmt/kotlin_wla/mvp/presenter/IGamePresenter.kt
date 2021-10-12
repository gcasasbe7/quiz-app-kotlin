package com.zmt.kotlin_wla.mvp.presenter

/**
 * Game presenter interface to provide the required actions to communicate the game UI with the game logic
 */
interface IGamePresenter {
    fun onProceedButtonTapped()
    fun onQuestionTimeOver(pSelectedAnswer : String)
    fun onQuizQuestionFragmentPresented()
}