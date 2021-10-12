package com.zmt.kotlin_wla.mvp.view

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.zmt.kotlin_wla.R
import com.zmt.kotlin_wla.mvp.helper.CountdownHelper
import com.zmt.kotlin_wla.mvp.helper.Utils
import com.zmt.kotlin_wla.mvp.model.QuizQuestion
import kotlin.math.roundToInt


/**
 * View fragment which represents one single question and answers of the quiz
 */
class QuizQuestionFragment(private val mQuizQuestion: QuizQuestion) : Fragment() {

    private lateinit var mView : View
    private lateinit var mRadioGroup : RadioGroup
    private lateinit var mProgressBar : ProgressBar
    private lateinit var mRemainingSeconds : TextView

    private lateinit var mCountDownTimer : CountdownHelper
    private var mProgress = 100

    private val CORRECT_COLOR   = Color.parseColor("#03DAC5")
    private val INCORRECT_COLOR = Color.parseColor("#F75D5D")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Fetch the view of the Fragment
        mView = inflater.inflate(R.layout.quiz_question_fragment_view, container, false)

        // Set the question
        mView.findViewById<TextView>(R.id.txt_quiz_question).text = mQuizQuestion.pQuestion

        // Set the answers
        mRadioGroup = mView.findViewById(R.id.rg_answers_container)

        // Dynamically build and style the child radio buttons
        for (i in 0 until mQuizQuestion.pAnswers.size) {
            val answerRadioButton  = RadioButton(context)
            answerRadioButton.gravity = Gravity.CENTER_VERTICAL
            answerRadioButton.textSize = 20f
            answerRadioButton.text = mQuizQuestion.pAnswers[i]
            answerRadioButton.id   = i
            answerRadioButton.setPadding(60,0,60,0)
            val lVerticalSpacing : Int = Utils.pxToDp(50)
            answerRadioButton.setPadding(0,lVerticalSpacing,0,lVerticalSpacing)

            // Insert the RadioButton view as a direct child of the parent RadioGroup
            mRadioGroup.addView(answerRadioButton)
        }

        // Initialize the Progress Bar - Timer
        mProgressBar = mView.findViewById(R.id.pb_question_timer)

        mRemainingSeconds = mView.findViewById(R.id.txt_remaining_seconds)
        mRemainingSeconds.text = (resources.getInteger(R.integer.seconds_per_question) + 1).toString()

        return mView
    }

    /**
     * Starts the Countdown for the current Quiz Question Fragment
     */
    fun startQuizQuestionTimer(pSeconds : Int) {
        // Init the timer
        mCountDownTimer = CountdownHelper(pSeconds, object : CountdownHelper.ICountdownCallback {
            // Update the progress
            override fun onTick() {
                mProgressBar.progress = mProgress
                mProgress -= (100f / pSeconds).roundToInt()
                mRemainingSeconds.text = (Integer.parseInt(mRemainingSeconds.text.toString()) - 1).toString()
            }

            // Handle time over scenario
            override fun onCountdownFinished() {
                // Fulfill the progress bar
                mProgressBar.progress = mProgress
                // Update the remaining time
                mRemainingSeconds.text = 0.toString()
                // todo: Handle failure when obtaining the GameActivity from the child Fragment. (Inefficient onResume)
                // Fetch the Game Activity
                val parentActivity = activity as GameActivity
                // Inform time's over
                parentActivity.questionTimeOver(getSelectedAnswer())
            }
        })

        // Start the timer
        mCountDownTimer.start()
    }

    /**
     * Stops the Countdown count for the current Quiz Question Fragment
     */
    fun stopQuizQuestionTimer() { mCountDownTimer.pause() }

    /**
     * Public method to get the selected answer for the current question
     */
    fun getSelectedAnswer() : String {
        return try {
            mView.findViewById<RadioButton>(mRadioGroup.checkedRadioButtonId).text.toString()
        }catch (ex : Exception) {
            ""
        }
    }

    /**
     * Public method to get the correct answer for the current question
     */
    fun getCorrectAnswer() : String { return mQuizQuestion.pCorrectAnswer }

    /**
     * Returns the data object for the current Quiz Question
     */
    fun getQuizQuestion() : QuizQuestion { return this.mQuizQuestion }

    /**
     * Method to enable or disable recursively all the views from the current Quiz Question Fragment
     * to prevent the user interacting with the view elements when required.
     */
    fun setViewEnabled(pViewGroup: ViewGroup?, enabled: Boolean) {
        val viewGroup = pViewGroup ?: mView.findViewById<LinearLayout>(R.id.quiz_fragment_root)
        val childCount = viewGroup.childCount
        for (i in 0 until childCount) {
            val view = viewGroup.getChildAt(i)
            view.isClickable = enabled
            if (view is ViewGroup) {
                setViewEnabled(view, enabled)
            }
        }
    }

    /**
     * Provides the visual feedback of a correct answer given.
     * Sets the background of the correct selected answer as green
     */
    fun onCorrectAnswerGiven(){
        val drawableBackground = GradientDrawable()
        drawableBackground.cornerRadius = 30f
        for(i in 0..mRadioGroup.childCount) {
            val lView = mRadioGroup.getChildAt(i)
            if(lView is RadioButton){
                if(lView.text.equals(getCorrectAnswer())){
                    drawableBackground.setColor(CORRECT_COLOR)
                    lView.setBackgroundDrawable(drawableBackground)
                }
            }
        }
    }

    /**
     * Provides the visual feedback of an incorrect answer given.
     * If an answer was given, sets the background of the correct selected answer as green and the incorrect as red
     * If no answer was given only the correct answer will be focused in error-style
     */
    fun onIncorrectAnswerGiven(pAnswerSelected : Boolean){
        for(i in 0..mRadioGroup.childCount) {
            val lView = mRadioGroup.getChildAt(i)
            if(lView is RadioButton){
                if(lView.text.toString().equals(getCorrectAnswer(), ignoreCase = false)){
                    val correctAnswerBackground = GradientDrawable()
                    correctAnswerBackground.cornerRadius = 30f
                    correctAnswerBackground.setColor(if(pAnswerSelected) CORRECT_COLOR else INCORRECT_COLOR)
                    lView.setBackgroundDrawable(correctAnswerBackground)
                } else if (lView.text.toString().equals(getSelectedAnswer(), ignoreCase = false)){
                    val incorrectAnswerBackground = GradientDrawable()
                    incorrectAnswerBackground.cornerRadius = 30f
                    incorrectAnswerBackground.setColor(INCORRECT_COLOR)
                    lView.setBackgroundDrawable(incorrectAnswerBackground)
                }
            }
        }
    }
}