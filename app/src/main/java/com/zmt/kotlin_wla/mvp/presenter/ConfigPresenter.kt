package com.zmt.kotlin_wla.mvp.presenter

import com.zmt.kotlin_wla.mvp.interactor.HTTPRequestTask
import com.zmt.kotlin_wla.mvp.helper.URLGenerator
import com.zmt.kotlin_wla.mvp.interactor.IRequestCallback
import com.zmt.kotlin_wla.mvp.helper.Utils
import com.zmt.kotlin_wla.mvp.model.QuizQuestion
import com.zmt.kotlin_wla.mvp.view.IConfigView
import org.json.JSONArray

/**
 * Presenter class to manage all the logic for the Game Configuration Screen
 */
class ConfigPresenter(var mView : IConfigView, private val mTotalQuestions : Int) : IConfigPresenter {

    // List of questions for the game
    private var mQuizQuestions = ArrayList<QuizQuestion>()

    // Static control data
    companion object {
        const val PARAMETER_INTENT_CODE   : Int = 1
        const val GAME_TYPE_PARAMETER     : Int = 10
        const val CATEGORY_PARAMETER      : Int = 11
        const val DIFFICULTY_PARAMETER    : Int = 12
    }

    /**
     * Given a game configuration by the user, prepares and starts the game
     */
    override fun onStartGamePressed(pCategory: String, pDifficulty: String, pGameType: String) {
        // Ensure the configuration is valid to proceed to start the game
        if ( securityCheck(pCategory, pDifficulty, pGameType) ) {

            // Clear any previous data
            mQuizQuestions.clear()

            mView.displayLoadingScreen(true)

            // Construct the URL
            val url = URLGenerator.getUrl(pCategory, pDifficulty, pGameType, mTotalQuestions)

            // Define the callback
            val requestCallback = object : IRequestCallback {
                /**
                 * Successful response
                 */
                override fun onSuccess(pResponse: JSONArray) {
                    // Store the data for the quiz
                    parseData(pResponse)
                    // Start the intent with the Parcelable data
                    mView.runGameIntent(mQuizQuestions)

                    mView.displayLoadingScreen(false)
                }

                /**
                 * Failure
                 */
                override fun onFailure(pError: String) {
                    mView.displayGeneralError(pError)
                    mView.displayLoadingScreen(false)
                }
            }

            // Execute the asynchronous HTTP GET Request on the Main UI Thread
            mView.runOnMainUIThread(Runnable { HTTPRequestTask(requestCallback).execute(url) })
        }
    }


    /**
     * Checks if the configuration is valid to proceed to start the game.
     * Returns a boolean indicating if we can proceed to start the game.
     * Also manages the output of the error message in case we can't proceed.
     */
    private fun securityCheck(pCategory: String, pDifficulty: String, pGameType: String) : Boolean {
        val UNSELECTED = "Select an option..."

        // Declare the boolean result
        var lResult = true

        // Check if the category value is valid
        if(pCategory == UNSELECTED){
            lResult = false
            mView.displayCategoryError("Please select a valid category")
        } else {
            mView.hideCategoryErrorText()
        }

        // Check if the difficulty value is valid
        if(pDifficulty == UNSELECTED){
            lResult = false
            mView.displayDifficultyError("Please select a valid difficulty")
        } else {
            mView.hideDifficultyErrorText()
        }

        // Check if the game type value is valid
        if(pGameType == UNSELECTED){
            lResult = false
            mView.displayGameTypeError("Please select a valid game type")
        } else {
            mView.hideGameTypeErrorText()
        }

        return lResult
    }

    /**
     * Parse the expected data from the response JSONArray and fills the member variable
     * list to store all the content
     */
    private fun parseData(pResponse : JSONArray) {

        val QUESTION_TAG : String           = "question"
        val CORRECT_ANSWER_TAG : String     = "correct_answer"
        val INCORRECT_ANSWER_TAG : String   = "incorrect_answers"

        for (i in 0 until pResponse.length()) {

            // Iterate the current item
            val item = pResponse.getJSONObject(i)

            // Fetch the necessary data to build the Question object
            val question : String               = Utils.decodeHTMLText(item.getString(QUESTION_TAG))
            val correctAnswer : String          = Utils.decodeHTMLText(item.getString(CORRECT_ANSWER_TAG))
            val incorrectAnswers : JSONArray    = item.get(INCORRECT_ANSWER_TAG) as JSONArray
            val answers : ArrayList<String>     = arrayListOf()

            // Group the answers
            for (j in 0 until incorrectAnswers.length()) {
                answers.add(Utils.decodeHTMLText(incorrectAnswers.getString(j)))
            }

            // Append the correct answer into the answer's array at a random position
            answers.add((0..answers.size).random(), correctAnswer)

            // Add the quiz question object
            mQuizQuestions.add(QuizQuestion(question, answers, correctAnswer))
        }
    }
}