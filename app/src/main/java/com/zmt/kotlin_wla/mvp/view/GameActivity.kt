package com.zmt.kotlin_wla.mvp.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.zmt.kotlin_wla.R
import com.zmt.kotlin_wla.mvp.model.QuizQuestion
import com.zmt.kotlin_wla.mvp.presenter.GamePresenter
import com.zmt.kotlin_wla.mvp.presenter.IGamePresenter

/**
 * Activity where the quiz game is performed. Contains and manages all the static data during the game
 * and also contains the stack of Fragments that form the whole quiz
 */
class GameActivity : AppCompatActivity(), IGameView {

    private lateinit var mPresenter : IGamePresenter

    private lateinit var mProceedButton : Button
    private lateinit var mQuestionCounterText : TextView
    private lateinit var mTopBar : RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        // Initialize view elements
        initViews()
        // Fetch the Intent data to build the presenter
        fetchIntentDataAndBuildPresenter()
    }

    /**
     * Disable android native back button action during the quiz game
     */
    override fun onBackPressed() {/*super.onBackPressed()*/ }

    /**
     * Initialize and define the view elements of the Game Screen
     */
    private fun initViews() {
        // Allocate and define the Proceed button behavior
        mProceedButton = findViewById(R.id.btn_proceed)
        mTopBar = findViewById(R.id.topBar)

        // Bind the proceed button action
        mProceedButton.setOnClickListener {
            mPresenter.onProceedButtonTapped()
        }

        // Allocate the question counter
        mQuestionCounterText = findViewById(R.id.txt_question_counter)
        mQuestionCounterText.text = "1 of ${resources.getInteger(R.integer.total_questions)}"
    }

    /**
     * Captures the extra data sent via the Bundle pack through the Activity Intent
     */
    private fun fetchIntentDataAndBuildPresenter() {
        // Fetch the quiz data
        val lQuizData = this.intent.extras?.getParcelableArrayList<QuizQuestion>("quizData")!!
        // Fetch the game parameter values
        val lGameType = this.intent.extras?.getString("type")!!
        val lGameCategory = this.intent.extras?.getString("category")!!
        val lGameDifficulty = this.intent.extras?.getString("difficulty")!!

        // Create the Game Presenter instance
        mPresenter = GamePresenter(this, lQuizData, resources, lGameType, lGameCategory, lGameDifficulty)
    }

    /**
     * Adds a new Quiz Question Fragment on top of the stack
     */
    override fun addQuizQuestionFragment(pQuizQuestionFragment: QuizQuestionFragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            .add(R.id.game_root_container, pQuizQuestionFragment)
            .addToBackStack(null)
            .commit()
    }

    /**
     * Handles the scenario where the user gives a correct answer to the last question
     */
    override fun correctAnswerGiven() {
        // Display animation/visual feedback of the success
        Toast.makeText(this, "Correct answer!", Toast.LENGTH_SHORT).show()
        // Transition to the next fragment
        transitionToNextFragment()
    }

    /**
     * Handles the scenario where the user gives an incorrect answer to the last question
     */
    override fun incorrectAnswerGiven() {
        // Display animation/visual feedback of the mistake
        Toast.makeText(this, "Incorrect answer...", Toast.LENGTH_SHORT).show()
        // Transition to the next fragment
        transitionToNextFragment()
    }

    /**
     * Performs the transition to the next QuizQuestionFragment after an answer is given
     */
    private fun transitionToNextFragment() {
        // Declare the runnable for the transition
        val runnable = Runnable {
            // Pop the fragment from the stack
            supportFragmentManager.popBackStack()
            // Synchronize the presenter
            mPresenter.onQuizQuestionFragmentPresented()
        }
        // Fetch the main thread handler
        val handler = Handler(Looper.getMainLooper())
        // Delay the transition runnable action to increase feedback time
        handler.postDelayed(runnable, 3000)
    }

    /**
     * Handles the scenario where the user wants to proceed without providing an answer for the current question
     */
    override fun noAnswerGiven() {
        Toast.makeText(
            this,
            "Please select an answer first",
            Toast.LENGTH_SHORT
        ).show()
    }

    /**
     * Handles the scenario where the user has not marked any answer and the time is up
     */
    override fun questionTimeOver(pSelectedAnswer : String) { mPresenter.onQuestionTimeOver(pSelectedAnswer) }

    /**
     * Redirects the user to the summary screen and destroys the current game activity
     */
    override fun onGameOver(pCorrectAnswers : Int, pIncorrectAnswers : Int, mGameType : String, mGameCategory : String, mGameDifficulty : String) {
        // Declare the Game Intent
        val intent = Intent(this, GameSummaryActivity::class.java)
        // Declare the data bundle
        val bundle = Bundle()
        // Bind the summary data into the bundle
        bundle.putInt("correctAnswers", pCorrectAnswers)
        bundle.putInt("incorrectAnswers", pIncorrectAnswers)
        bundle.putString("type", mGameType)
        bundle.putString("category", mGameCategory)
        bundle.putString("difficulty", mGameDifficulty)
        // Attach the data bundle into the intent
        intent.putExtras(bundle)
        // Run the intent
        startActivity(intent)
        // Finish the game Activity
        finish()
    }

    /**
     * Increase the question index in the top bar counter. Max to 10
     */
    override fun increaseQuestionCounter(pTotalQuestions: Int) {
        val increased = Integer.valueOf(mQuestionCounterText.text.toString().split(" ")[0]) + 1
        if(increased <= pTotalQuestions) { mQuestionCounterText.text = "$increased of $pTotalQuestions" }
    }

    /**
     * Recursive method to enable or disable the ability to interact with any view element from the screen to have a better
     * control of the game flow.
     */
    override fun setViewEnabled(pViewGroup: ViewGroup?, pEnabled: Boolean) {
        val viewGroup = pViewGroup ?: findViewById<LinearLayout>(R.id.main_container)
        val childCount = viewGroup.childCount
        for (i in 0 until childCount) {
            val view = viewGroup.getChildAt(i)
            view.isClickable = pEnabled
            if (view is ViewGroup) {
                setViewEnabled(view, pEnabled)
            }
        }
    }
}