package com.zmt.kotlin_wla.mvp.presenter

/**
 * Main screen listener to fetch the game config and start building the game initial state.
 */
interface IConfigPresenter {
    fun onStartGamePressed(pCategory : String, pDifficulty : String, pGameType : String)
}

/**
 * Listener used to configure all the required parameters to start the game
 */
interface IGameParameterListener {
    fun onOptionSelected(pSelectedOption : String)
    fun onExit()
}