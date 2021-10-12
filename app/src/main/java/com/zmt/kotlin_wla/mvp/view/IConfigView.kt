package com.zmt.kotlin_wla.mvp.view

import com.zmt.kotlin_wla.mvp.model.QuizQuestion

/**
 * View Interface to allow the communication between the configuration presenter
 * and the configuration screen view.
 */
interface IConfigView {
    fun displayGameTypeError(pMessage : String)
    fun displayCategoryError(pMessage : String)
    fun displayDifficultyError(pMessage : String)
    fun displayGeneralError(pMessage : String)
    fun hideGameTypeErrorText()
    fun hideDifficultyErrorText()
    fun hideCategoryErrorText()
    fun runGameIntent(data : ArrayList<QuizQuestion>)
    fun runOnMainUIThread(pRunnable: Runnable)
    fun displayLoadingScreen(pShow : Boolean)
}