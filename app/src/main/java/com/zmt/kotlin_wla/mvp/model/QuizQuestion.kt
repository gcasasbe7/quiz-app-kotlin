package com.zmt.kotlin_wla.mvp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Parcelable model data class to represent a quiz question object
 */
@Parcelize data class QuizQuestion(val pQuestion : String, val pAnswers : ArrayList<String>, val pCorrectAnswer : String) : Parcelable