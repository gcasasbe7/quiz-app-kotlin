package com.zmt.kotlin_wla.mvp.helper

import android.os.CountDownTimer

/**
 * Class to manage a count-down timer for each QuizQuestion.
 * Provides the necessary callbacks to manage the timed actions
 */
class CountdownHelper(var mSeconds : Int, var mCallback: ICountdownCallback) {

    // Define the count down timer behavior
    private var mCountdownTimer : CountDownTimer = object : CountDownTimer(mSeconds * 1000L, 1000) {

        // Update UI status after 1 second passed
        override fun onTick(p0: Long) { mCallback.onTick() }

        // Invoke the callback finish action when timer is finished
        override fun onFinish() { mCallback.onCountdownFinished() }
    }

    /**
     * Inner Interface to provide information to the parent QuizFragment about the estate of the CountDownTimer object
     */
    interface ICountdownCallback {
        fun onTick()
        fun onCountdownFinished()
    }

    /**
     * Starts the Count Down Timer
     */
    fun start() { mCountdownTimer.start() }

    /**
     * Pauses the Count Down Timer
     */
    fun pause() { mCountdownTimer.cancel() }
}