package com.zmt.kotlin_wla.mvp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zmt.kotlin_wla.R
import java.util.*

/**
 * Activity to display the summary of the game.
 * Shows the user a friendly-layout with the amount of correct and incorrect answers of his game
 */
class GameSummaryActivity : AppCompatActivity() {

    private lateinit var mCorrectAnswersText    : TextView
    private lateinit var mIncorrectAnswersText  : TextView
    private lateinit var mParamsSummaryText     : TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_summary)

        // Fetch the view pointers
        mCorrectAnswersText     = findViewById(R.id.txt_total_correct_answers)
        mIncorrectAnswersText   = findViewById(R.id.txt_total_incorrect_answers)
        mParamsSummaryText      = findViewById(R.id.game_parameters_summary)

        // Set the values
        mCorrectAnswersText.text    = getCorrectAnswers().toString()
        mIncorrectAnswersText.text  = getIncorrectAnswers().toString()
        mParamsSummaryText.text = "${getGameCategory()} (${getGameDifficulty()}) - ${getGameType()}".toUpperCase(
            Locale.ROOT)
    }

    /**
     * Finishes the Game Summary Screen presenting the user the Main Activity again.
     * * Bind to the button via XML.
     */
    fun playAgain(view: View) { finish() }

    /**
     * Fetches the number of correct answers given throughout the test from the Intent bundle data
     */
    private fun getCorrectAnswers() : Int { return this.intent.extras?.getInt("correctAnswers")!! }

    /**
     * Fetches the number of incorrect answers given throughout the test from the Intent bundle data
     */
    private fun getIncorrectAnswers() : Int { return this.intent.extras?.getInt("incorrectAnswers")!! }
    /**
     * Fetches the game type param from the Intent bundle data
     */
    private fun getGameType() : String { return this.intent.extras?.getString("type")!! }
    /**
     * Fetches the game category param from the Intent bundle data
     */
    private fun getGameCategory() : String { return this.intent.extras?.getString("category")!! }
    /**
     * Fetches the game difficulty param from the Intent bundle data
     */
    private fun getGameDifficulty() : String { return this.intent.extras?.getString("difficulty")!! }
}