package com.zmt.kotlin_wla.mvp.helper

import android.content.res.Resources
import android.text.Html

/**
 * Helper class to perform basic utility operations like sanity checks, decoding and conversions
 */
object Utils {

    /**
     * Decodes an Encoded HTML Text
     */
    fun decodeHTMLText(pHTMLText : String) : String {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(pHTMLText, Html.FROM_HTML_MODE_LEGACY).toString()
        } else {
            pHTMLText
        }
    }

    /**
     * Ensures the given String is valid and functional
     */
    fun isValidEntity(pValue : String) : Boolean {
        return pValue != null && pValue.isNotEmpty()
    }

    /**
     * Converts an Integer value from Pixels to Density Pixels
     */
    fun pxToDp(px: Int): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }

    /**
     * Converts an Integer value from Density Pixels to Pixels
     */
    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
}