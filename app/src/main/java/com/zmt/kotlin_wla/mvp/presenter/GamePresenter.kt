package com.zmt.kotlin_wla.mvp.presenter

import android.content.res.Resources
import com.zmt.kotlin_wla.R
import com.zmt.kotlin_wla.mvp.model.QuizQuestion
import com.zmt.kotlin_wla.mvp.helper.Utils
import com.zmt.kotlin_wla.mvp.view.IGameView
import com.zmt.kotlin_wla.mvp.view.QuizQuestionFragment

/**
 * Presenter class to manage all the state control data throughout the game.
 * Handles and persists all the data and flags to ensure an efficient and expected game behavior
 */
class GamePresenter(private val mGameView : IGameView, mData : ArrayList<QuizQuestion>, mGameResources : Resources,
                    private val mGameType : String, private val mGameCategory : String, private val mGameDifficulty : String) : IGamePresenter {

    private var mCurrentQuizQuestionFragment : QuizQuestionFragment
    private var mQuizQuestionFragments       : ArrayList<QuizQuestionFragment> = arrayListOf()
    private var mCorrectAnswersCount         : Int = 0
    private var mIncorrectAnswersCount       : Int = 0

    // Game config
    private var mTimePerQuestion : Int = mGameResources.getInteger(R.integer.seconds_per_question)
    private var mTotalQuestions  : Int = mGameResources.getInteger(R.integer.total_questions)

    /**
     * Secondary constructor to build the QuizQuestionFragments stack
     * Initializes and persists the currently displayed QuizQuestionFragment
     */
    init {
        for (question : QuizQuestion in mData) {
            // Build the Quiz Question Fragment with the Quiz Question
            val iQuizQuestionFragment = QuizQuestionFragment(question)
            // Persist into our controlled stack of fragments
            mQuizQuestionFragments.add(iQuizQuestionFragment)
            // Attach the fragment into the view
            mGameView.addQuizQuestionFragment(iQuizQuestionFragment)
        }
        // Persist the current quiz question we are displaying on the top of the stack
        mCurrentQuizQuestionFragment = getCurrentQuizQuestionFragment()
        // Start the timer for the first question
        mCurrentQuizQuestionFragment.startQuizQuestionTimer(mTimePerQuestion)
    }

    /**
     * Method to manage the progress of the user throughout the quiz
     * Ensures that an answer is given to the current Quiz Question
     * Verifies the results and carry to the next stage of the Quiz
     */
    override fun onProceedButtonTapped() {
        // Ensure there is a valid selected answer
        val lSelectedAnswer = mCurrentQuizQuestionFragment.getSelectedAnswer()

        if (Utils.isValidEntity(lSelectedAnswer)) {

            // Block the Game Activity AND the current fragment for further interactions
            setBlockTouchEvents(pIsEnabled = false)

            // Stop the current Quiz Question Fragment timer
            mCurrentQuizQuestionFragment.stopQuizQuestionTimer()

            // An answer has been selected, perform the check
            if (lSelectedAnswer.equals(mCurrentQuizQuestionFragment.getCorrectAnswer(), ignoreCase = false)) {
                // Correct answer
                onCorrectAnswerGiven()

            } else {
                // Incorrect answer
                onIncorrectAnswerGiven(pAnswerSelected = true)
            }
        } else {
            // No answer has been selected, display message
            mGameView.noAnswerGiven()
        }
    }

    /**
     * Method to enable or disable touch events to the screen.
     * Used for better control and UX during the game.
     */
    private fun setBlockTouchEvents(pIsEnabled : Boolean) {
        mGameView.setViewEnabled(null, pIsEnabled)
        mCurrentQuizQuestionFragment.setViewEnabled(null, pIsEnabled)
    }

    /**
     * Handler method to manage the scenario where the user runs out of time in a Quiz Question
     */
    override fun onQuestionTimeOver(pSelectedAnswer : String) {
        // Block the touch events for the current question
        setBlockTouchEvents(pIsEnabled = false)
        // Is there a valid selected answer?
        if(Utils.isValidEntity(pSelectedAnswer)){
            // Is the answer correct?
            if(pSelectedAnswer.equals(mCurrentQuizQuestionFragment.getCorrectAnswer(), ignoreCase = false)) {
                // The selected answer correct
                onCorrectAnswerGiven()
            } else {
                // The selected answer is incorrect
                onIncorrectAnswerGiven(pAnswerSelected = true)
            }
        } else {
            // No selected answer treated as incorrect, display feedback
            onIncorrectAnswerGiven(pAnswerSelected = false)
        }
    }

    /**
     * Method to update the control and view data to handle correct answer scenarios
     */
    private fun onCorrectAnswerGiven() {
        // Update control data
        mCorrectAnswersCount++
        // Provide feedback to the user
        mGameView.correctAnswerGiven()
        mCurrentQuizQuestionFragment.onCorrectAnswerGiven()
    }

    /**
     * Method to update the control and view data to handle incorrect answer scenarios
     */
    private fun onIncorrectAnswerGiven(pAnswerSelected : Boolean) {
        // Update control data
        mIncorrectAnswersCount++
        // Provide feedback to the user
        mGameView.incorrectAnswerGiven()
        mCurrentQuizQuestionFragment.onIncorrectAnswerGiven(pAnswerSelected)
    }

    /**
     * Manages the view and the state data once the new Quiz Question Fragment has been presented
     */
    override fun onQuizQuestionFragmentPresented() {
        // Refresh the question counter
        mGameView.increaseQuestionCounter(mTotalQuestions)
        // Refresh the state data
        advanceQuizStep()
    }

    /**
     * Advances one step forward in the Quiz. Refreshes all the necessary state data to proceed.
     * If there is no QuizQuestionFragment to proceed to, triggers the end of game scenario and finishes the Game
     */
    private fun advanceQuizStep() {
        // Remove previous question
        mQuizQuestionFragments.removeAt(mQuizQuestionFragments.size - 1)

        // Can we proceed to the next quiz question?
        if (mQuizQuestionFragments.isEmpty()) {
            // All questions of the quiz have been completed
            mGameView.onGameOver(mCorrectAnswersCount, mIncorrectAnswersCount, mGameType, mGameCategory, mGameDifficulty)
        } else {
            // Refresh the current
            mCurrentQuizQuestionFragment = getCurrentQuizQuestionFragment()

            // Unblock screen touch events
            setBlockTouchEvents(pIsEnabled = true)

            // Start the timer for the new Quiz Question Fragment
            mCurrentQuizQuestionFragment.startQuizQuestionTimer(mTimePerQuestion)
        }
    }

    /**
     * Returns the current Quiz Question Fragment displayed on the top of the stack. Always the last one of the list.
     */
    private fun getCurrentQuizQuestionFragment() : QuizQuestionFragment { return mQuizQuestionFragments[mQuizQuestionFragments.size - 1] }
}